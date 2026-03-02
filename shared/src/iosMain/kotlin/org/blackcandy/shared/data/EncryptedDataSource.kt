package org.blackcandy.shared.data

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.interpretObjCPointer
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFBooleanTrue
import platform.CoreFoundation.kCFTypeDictionaryKeyCallBacks
import platform.CoreFoundation.kCFTypeDictionaryValueCallBacks
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
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

    actual fun getApiToken(): String? {
        val query = CFDictionaryCreateMutable(null, 0, kCFTypeDictionaryKeyCallBacks.ptr, kCFTypeDictionaryValueCallBacks.ptr)
        val keyRef = CFBridgingRetain(API_TOKEN_KEY as Any as NSString)

        CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionaryAddValue(query, kSecAttrAccount, keyRef)
        CFDictionaryAddValue(query, kSecReturnData, kCFBooleanTrue)
        CFDictionaryAddValue(query, kSecMatchLimit, kSecMatchLimitOne)

        val result =
            memScoped {
                val resultVar = alloc<CFTypeRefVar>()
                val status = SecItemCopyMatching(query, resultVar.ptr)

                if (status == errSecSuccess) {
                    val data = resultVar.value?.let { interpretObjCPointer<NSData>(it.rawValue) }
                    data?.let {
                        NSString.create(it, NSUTF8StringEncoding)?.toString()
                    }
                } else {
                    null
                }
            }

        CFRelease(query)
        CFRelease(keyRef)

        return result
    }

    actual fun updateApiToken(apiToken: String) {
        val apiTokenData = (apiToken as Any as NSString).dataUsingEncoding(NSUTF8StringEncoding) ?: return

        val query = CFDictionaryCreateMutable(null, 0, kCFTypeDictionaryKeyCallBacks.ptr, kCFTypeDictionaryValueCallBacks.ptr)
        val keyRef = CFBridgingRetain(API_TOKEN_KEY as Any as NSString)
        val valueRef = CFBridgingRetain(apiTokenData)

        CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionaryAddValue(query, kSecAttrAccount, keyRef)

        SecItemDelete(query)

        CFDictionaryAddValue(query, kSecValueData, valueRef)
        SecItemAdd(query, null)

        CFRelease(query)
        CFRelease(keyRef)
        CFRelease(valueRef)
    }

    actual fun removeApiToken() {
        val query = CFDictionaryCreateMutable(null, 0, kCFTypeDictionaryKeyCallBacks.ptr, kCFTypeDictionaryValueCallBacks.ptr)
        val keyRef = CFBridgingRetain(API_TOKEN_KEY as Any as NSString)

        CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionaryAddValue(query, kSecAttrAccount, keyRef)

        SecItemDelete(query)

        CFRelease(query)
        CFRelease(keyRef)
    }
}
