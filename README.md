# SSL / TLS Certificate Pinning (XML + OkHttp) – Example Project

This project demonstrates **three** related pieces:

1. **XML Network Security Config** (framework-level certificate pinning)
2. **OkHttp Certificate Pinning** using `CertificatePinner`  
   (`sslProvider.createCertificatePinning()`)
3. **OkHttp Custom TrustManager / SSLSocketFactory** using `addSSLSocketFactory(context)`

You can use **each one separately** or **combine them** for extra security.

---

## 1. Project Overview

### Key Classes / Files

- `SslProvider.kt`  
  Creates a `CertificatePinner` for OkHttp.

- `RetrofitBuilder.kt`  
  Builds an `OkHttpClient` and `Retrofit` with:
  - optional `certificatePinner(...)`
  - optional `addSSLSocketFactory(context)`

- `MainActivity.kt`  
  Example of calling the API.

- `res/xml/network_security_config.xml`  
  XML-based network security & pinning.

- `AndroidManifest.xml`  
  Connects the XML config to the application.

- `assets/certificate_pin`  
  PEM certificate(s) used by `addSSLSocketFactory`.

---

## 2. XML Method – `network_security_config.xml`

### 2.1 Create XML Config

`app/src/main/res/xml/network_security_config.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="false">
        <!-- Your backend domain -->
        <domain includeSubdomains="true">supardating.com</domain>

        <!-- Certificate pinning -->
        <pin-set expiration="2027-12-31">
            <!-- Primary pin (SHA-256 of public key) -->
            <pin digest="SHA-256">zILjrdFTDD8eAW37ktqlQWYd37Ppy8/bVLEPfNXtv7Q=</pin>

            <!-- Optional: backup pin -->
            <!-- <pin digest="SHA-256">YOUR_BACKUP_PIN_BASE64==</pin> -->
        </pin-set>
    </domain-config>
</network-security-config>
