package org.blackcandy.shared.data

import org.blackcandy.shared.api.BlackCandyService
import org.blackcandy.shared.models.SystemInfo
import org.blackcandy.shared.utils.TaskResult

class SystemInfoRepository(
    private val service: BlackCandyService,
) {
    suspend fun getSystemInfo(): TaskResult<SystemInfo> {
        return service.getSystemInfo().asResult()
    }
}
