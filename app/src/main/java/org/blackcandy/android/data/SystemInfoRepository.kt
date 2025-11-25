package org.blackcandy.android.data

import org.blackcandy.android.api.BlackCandyService
import org.blackcandy.shared.models.SystemInfo
import org.blackcandy.shared.utils.TaskResult

class SystemInfoRepository(
    private val service: BlackCandyService,
) {
    suspend fun getSystemInfo(): TaskResult<SystemInfo> = service.getSystemInfo().asResult()
}
