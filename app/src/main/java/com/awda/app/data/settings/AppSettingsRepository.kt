package com.awda.app.data.settings

import com.awda.app.data.challenge.models.InstalledApp
import com.awda.app.data.common.models.Resource
import com.awda.app.data.common.repository.SettingsRepository
import com.awda.app.data.settings.db.BlockedAppDao
import com.awda.app.data.settings.db.SecureAppDao
import com.awda.app.data.settings.models.SecureApp
import com.awda.app.domain.processors.AppUsageProcessor
import com.awda.app.domain.processors.PreferencesProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

/**
 * Created by Abdelrahman Rizq
 */

class AppSettingsRepository(
    private val processor: PreferencesProcessor,
    private val appUsageProcessor: AppUsageProcessor,
    private val blockDao: BlockedAppDao,
    private val secureDao: SecureAppDao,
) :
    SettingsRepository(processor) {
    fun setUsageAlert(enabled: Boolean) {
        processor.setUsageAlert(enabled)
    }

    fun getUsageAlert() = processor.getUsageAlert()

    fun setBlockedWebsites(sites: List<String>) {
        processor.setBlockedWebsites(sites)
    }

    fun getBlockedWebsites() = processor.getBlockedWebsites()

    suspend fun setBlockedApp(app: InstalledApp) =
        withContext(Dispatchers.IO) { blockDao.insert(app) }

    suspend fun getBlockedApps() = withContext(Dispatchers.IO) { blockDao.retrieve() }

    suspend fun deleteBlockedApp(app: InstalledApp) =
        withContext(Dispatchers.IO) { blockDao.delete(app) }

    suspend fun setSecureApp(app: SecureApp) = withContext(Dispatchers.IO) { secureDao.insert(app) }

    suspend fun getSecureApps() = withContext(Dispatchers.IO) { secureDao.retrieve() }

    suspend fun deleteSecureApp(app: SecureApp) =
        withContext(Dispatchers.IO) { secureDao.delete(app) }

    suspend fun getInstalledApps() = flow {
        emit(Resource.Success(appUsageProcessor.sortedInstalledApps()))
    }
}