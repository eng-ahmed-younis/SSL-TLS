package com.example.ssl_tls.data.data_source.romote.service

import okhttp3.CertificatePinner

class SslProvider {

    fun createCertificatePinning(): CertificatePinner {
        // in ssl check hand when server send certificate included public key shout it equal this key
        //  "sha256/zILjrdFTDD8eAW37ktqlQWYd37Ppy8/bVLEPfNXtv7Q="
        return CertificatePinner.Builder()
            .add(
                pattern = "supardating.com",
                "sha256/zILjrdFTDD8eAW37ktqlQWYd37Ppy8/bVLEPfNXtv7Q="
            ).build()
    }
}
