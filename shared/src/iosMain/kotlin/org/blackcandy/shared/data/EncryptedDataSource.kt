package org.blackcandy.shared.data

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.interpretObjCPointer
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFTypeRef
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFBooleanTrue
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSDictionary
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccount
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual class EncryptedDataSource {
    companion object {
        private const val API_TOKEN_KEY = "org.blackcandy.api_token_key"
    }

    private fun CFTypeRef?.asNSObject(): Any? = this?.let { interpretObjCPointer<Any>(it.rawValue) }

    actual fun getApiToken(): String? {
        val query =
            mapOf<Any?, Any?>(
                kSecClass.asNSObject() to kSecClassGenericPassword.asNSObject(),
                kSecAttrAccount.asNSObject() to API_TOKEN_KEY,
                kSecReturnData.asNSObject() to kCFBooleanTrue.asNSObject(),
                kSecMatchLimit.asNSObject() to kSecMatchLimitOne.asNSObject(),
            ) as NSDictionary

        return memScoped {
            val result = alloc<CFTypeRefVar>()
            val cfQuery = CFBridgingRetain(query) as CFDictionaryRef
            val status = SecItemCopyMatching(cfQuery, result.ptr)

            if (status == errSecSuccess) {
                val data = result.value?.let { interpretObjCPointer<NSData>(it.rawValue) }
                data?.let {
                    NSString.create(it, NSUTF8StringEncoding)?.toString()
                }
            } else {
                null
            }
        }
    }

    actual fun updateApiToken(apiToken: String) {
        removeApiToken()

        val apiTokenData = (apiToken as Any as NSString).dataUsingEncoding(NSUTF8StringEncoding) ?: return
        val query =
            mapOf<Any?, Any?>(
                kSecClass.asNSObject() to kSecClassGenericPassword.asNSObject(),
                kSecAttrAccount.asNSObject() to API_TOKEN_KEY,
                kSecValueData.asNSObject() to apiTokenData,
            ) as NSDictionary

        val cfQuery = CFBridgingRetain(query) as CFDictionaryRef
        SecItemAdd(cfQuery, null)
    }

    actual fun removeApiToken() {
        val query =
            mapOf<Any?, Any?>(
                kSecClass.asNSObject() to kSecClassGenericPassword.asNSObject(),
                kSecAttrAccount.asNSObject() to API_TOKEN_KEY,
            ) as NSDictionary

        val cfQuery = CFBridgingRetain(query) as CFDictionaryRef
        SecItemDelete(cfQuery)
    }
}
