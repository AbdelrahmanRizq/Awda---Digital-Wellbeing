package com.awda.app.di

import android.app.usage.UsageStatsManager
import android.content.Context
import com.awda.app.common.Constants
import com.awda.app.data.challenge.ChallengeRepository
import com.awda.app.data.challenge.db.ChallengeRoomDatabase
import com.awda.app.data.common.repository.SettingsRepository
import com.awda.app.data.home.HomeRepository
import com.awda.app.data.home.db.AppUsageRoomDatabase
import com.awda.app.data.intro.AppIntroRepository
import com.awda.app.data.settings.AppSettingsRepository
import com.awda.app.data.settings.db.BlockedAppRoomDatabase
import com.awda.app.data.settings.db.SecureAppRoomDatabase
import com.awda.app.data.usage.AppUsageRepository
import com.awda.app.domain.challenge.usecase.EnableChallengeUseCase
import com.awda.app.domain.challenge.usecase.InstalledAppsUseCase
import com.awda.app.domain.challenge.usecase.RetrieveChallengesUseCase
import com.awda.app.domain.challenge.usecase.SetChallengeUseCase
import com.awda.app.domain.common.usecase.GetUsageTimeLimitUseCase
import com.awda.app.domain.common.usecase.SetUsageTimeLimitUseCase
import com.awda.app.domain.home.usecase.AddictionLevelUseCase
import com.awda.app.domain.home.usecase.AppUsageUseCase
import com.awda.app.domain.home.usecase.MonthAverageUsageUseCase
import com.awda.app.domain.home.usecase.UsageChartUseCase
import com.awda.app.domain.home.usecase.UsageProgressUseCase
import com.awda.app.domain.home.usecase.WeekAverageUsageUseCase
import com.awda.app.domain.intro.usecase.GetAppIntroCompletedUseCase
import com.awda.app.domain.intro.usecase.SetAppIntroCompletedUseCase
import com.awda.app.domain.processors.AppUsageProcessor
import com.awda.app.domain.processors.ChartProcessor
import com.awda.app.domain.processors.PreferencesProcessor
import com.awda.app.domain.settings.usecase.DeleteBlockedAppUseCase
import com.awda.app.domain.settings.usecase.DeleteSecureAppUseCase
import com.awda.app.domain.settings.usecase.GetBlockedAppsUseCase
import com.awda.app.domain.settings.usecase.GetBlockedWebsitesUseCase
import com.awda.app.domain.settings.usecase.GetSecureAppsUseCase
import com.awda.app.domain.settings.usecase.GetUsageAlertUseCase
import com.awda.app.domain.settings.usecase.SetBlockedAppUseCase
import com.awda.app.domain.settings.usecase.SetBlockedWebsitesUseCase
import com.awda.app.domain.settings.usecase.SetSecureAppUseCase
import com.awda.app.domain.settings.usecase.SetUsageAlertUseCase
import com.awda.app.domain.usage.usecase.AppMonthAverageUsagePerDayUseCase
import com.awda.app.domain.usage.usecase.AppMonthTotalUsageUseCase
import com.awda.app.domain.usage.usecase.AppUsageChartUseCase
import com.awda.app.domain.usage.usecase.AppWeekAverageUsagePerDayUseCase
import com.awda.app.domain.usage.usecase.AppWeekTotalUsageUseCase
import com.awda.app.presentation.challenge.ChallengeViewModel
import com.awda.app.presentation.common.components.LockerDialog
import com.awda.app.presentation.home.HomeViewModel
import com.awda.app.presentation.intro.AppIntroViewModel
import com.awda.app.presentation.settings.SettingsViewModel
import com.awda.app.presentation.usage.AppUsageViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Abdelrahman Rizq
 */

val appModule = module {

    //Processors
    single {
        PreferencesProcessor(
            androidContext().getSharedPreferences(
                Constants.APP_INTRO_PREFERENCES,
                Context.MODE_PRIVATE
            )
        )
    }

    single {
        AppUsageProcessor(
            androidContext(),
            androidContext().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        )
    }

    single { ChartProcessor() }

    //Databases
    single { AppUsageRoomDatabase.getInstance(androidContext()) }

    single { ChallengeRoomDatabase.getInstance(androidContext()) }

    single { BlockedAppRoomDatabase.getInstance(androidContext()) }

    single { SecureAppRoomDatabase.getInstance(androidContext()) }

    factory { get<AppUsageRoomDatabase>().dao() }

    factory { get<ChallengeRoomDatabase>().dao() }

    factory { get<BlockedAppRoomDatabase>().dao() }

    factory { get<SecureAppRoomDatabase>().dao() }

    //Repositories
    single { AppIntroRepository(get()) }

    single { AppSettingsRepository(get(), get(), get(), get()) }

    single { SettingsRepository(get()) }

    single { HomeRepository(androidContext(), get(), get(), get(), get()) }

    single { AppUsageRepository(androidContext(), get(), get(), get()) }

    single { ChallengeRepository(androidContext(), get(), get()) }

    //UseCases
    factory { AppUsageUseCase(get()) }

    factory { AddictionLevelUseCase(get()) }

    factory { UsageProgressUseCase(get()) }

    factory { UsageChartUseCase(get()) }

    factory { WeekAverageUsageUseCase(get()) }

    factory { MonthAverageUsageUseCase(get()) }

    factory { GetAppIntroCompletedUseCase(get()) }

    factory { SetAppIntroCompletedUseCase(get()) }

    factory { GetUsageTimeLimitUseCase(get()) }

    factory { SetUsageTimeLimitUseCase(get()) }

    factory { AppMonthAverageUsagePerDayUseCase(get()) }

    factory { AppMonthTotalUsageUseCase(get()) }

    factory { AppWeekAverageUsagePerDayUseCase(get()) }

    factory { AppWeekTotalUsageUseCase(get()) }

    factory { AppUsageChartUseCase(get()) }

    factory { SetUsageAlertUseCase(get()) }

    factory { GetUsageAlertUseCase(get()) }

    factory { SetChallengeUseCase(get()) }

    factory { InstalledAppsUseCase(get()) }

    factory { RetrieveChallengesUseCase(get()) }

    factory { EnableChallengeUseCase(get()) }

    factory { com.awda.app.domain.settings.usecase.InstalledAppsUseCase(get()) }

    factory { SetBlockedAppUseCase(get()) }

    factory { GetBlockedAppsUseCase(get()) }

    factory { SetBlockedWebsitesUseCase(get()) }

    factory { GetBlockedWebsitesUseCase(get()) }

    factory { DeleteBlockedAppUseCase(get()) }

    factory { SetSecureAppUseCase(get()) }

    factory { GetSecureAppsUseCase(get()) }

    factory { DeleteSecureAppUseCase(get()) }

    //UI
    factory { LockerDialog(androidContext()) }

    //ViewModels
    viewModel { AppIntroViewModel(get(), get(), get(), get()) }

    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get()) }

    viewModel { AppUsageViewModel(get(), get(), get(), get(), get()) }

    viewModel {
        SettingsViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    viewModel { ChallengeViewModel(get(), get(), get(), get()) }
}