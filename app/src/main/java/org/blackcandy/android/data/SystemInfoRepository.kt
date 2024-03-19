package org.blackcandy.android.data

import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.android.models.SystemInfo
import org.blackcandy.android.utils.TaskResult

class SystemInfoRepository(
    private val service: BlackCandyService,
) {
    suspend fun getSystemInfo(): TaskResult<SystemInfo> {
        return service.getSystemInfo().asResult()
    }
}
