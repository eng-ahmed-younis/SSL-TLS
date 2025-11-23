package com.example.ssl_tls.data.data_source.romote.service

import android.content.Context
import android.util.Log
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class SslProvider {


    // method 1
    fun createCertificatePinning(): CertificatePinner {
        // Create a CertificatePinner instance to enforce public key pinning
        // When the server sends its certificate during the SSL handshake,
        // OkHttp will compare the certificate's public key hash with this value.
        // If they don't match, the connection will be rejected.
        return CertificatePinner.Builder()
            .add(
                // Hostname (without scheme, just the domain)
                pattern = "supardating.com",
                // SHA-256 hash of the server certificate's public key
                "sha256/zILjrdFTDD8eAW37ktqlQWYd37Ppy8/bVLEPfNXtv7Q="
            )
            .build()
    }
}

// method 2

// Extension function on OkHttpClient.Builder to configure a custom SSL socket factory
// using certificates stored in the app's assets.
fun OkHttpClient.Builder.addSSLSocketFactory(
    context: Context
) = apply {
    // Factory that understands X.509 certificates (standard for SSL/TLS)
    val certificateFactory = CertificateFactory.getInstance("X.509")

    // Create an empty in-memory KeyStore to hold our trusted certificates
    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
        // Initialize KeyStore with no existing data (null input stream and password)
        load(null, null)
    }

    // The `use` function ensures the InputStream is closed automatically
    // after we're done reading from "certificate_pin" in the assets folder.
    context.assets.open("certificate_pin").use { inputStream ->
        // Read the entire certificate file as text
        val certificates = inputStream.bufferedReader().use { it.readText() }
            // Split by the end of certificate delimiter, because we might have multiple certs in one file
            .split("-----END CERTIFICATE-----")
            // Remove whitespace-only entries
            .map { it.trim() }
            .filterNot { it.isEmpty() }
            // Re-append the "END CERTIFICATE" line, since split removed it
            .map { "$it\n-----END CERTIFICATE-----" }

        // Loop through each PEM-encoded certificate we extracted
        certificates.forEachIndexed { index, certificatePEM ->
            // Convert the PEM string back into an InputStream
            val certificateInputStream = certificatePEM.byteInputStream()
            try {
                // Parse the PEM data into a Java X.509 Certificate object
                val certificate = certificateFactory.generateCertificate(certificateInputStream)

                // Give each certificate a unique alias in the KeyStore
                val alias = "cer_$index"

                // Store the certificate in our KeyStore as a trusted certificate
                keyStore.setCertificateEntry(alias, certificate)

            } catch (exception: Exception) {
                // Log any errors that occur while parsing/adding certificates
                Log.d("TAG", "addSSLSocketFactory error: ${exception.message}")
            }
        }

        // Build a TrustManagerFactory that uses the certificates in our KeyStore
        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                // Initialize the TrustManagerFactory with our custom KeyStore
                init(keyStore)
            }

        // Get the first (and typically only) X509TrustManager from the factory
        val trustManager = trustManagerFactory.trustManagers[0] as X509TrustManager

        // Create an SSLContext configured with our custom TrustManager
        val sslContext = SSLContext.getInstance("TLSv1.3").apply {
            // The first parameter is for custom key managers (null uses default),
            // second is our trust managers, third is the source of randomness.
            init(null, arrayOf(trustManager), SecureRandom())
        }

        // Tell OkHttp to use our SSLContext's socket factory and the associated TrustManager.
        // This makes OkHttp trust only the certificates we loaded into the KeyStore.
        sslSocketFactory(sslContext.socketFactory, trustManager)
    }
}
