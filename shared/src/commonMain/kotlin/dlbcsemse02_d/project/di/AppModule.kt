package dlbcsemse02_d.project.di

import dlbcsemse02_d.project.application.service.ModeratorService
import dlbcsemse02_d.project.application.service.SongService
import dlbcsemse02_d.project.domain.repository.ModeratorRepository
import dlbcsemse02_d.project.domain.repository.SongRepository
import dlbcsemse02_d.project.domain.repository.SongRequestRepository
import dlbcsemse02_d.project.infrastructure.repository.MockDataStore
import dlbcsemse02_d.project.infrastructure.repository.MockModeratorRepository
import dlbcsemse02_d.project.infrastructure.repository.MockSongRepository
import dlbcsemse02_d.project.infrastructure.repository.MockSongRequestRepository
import dlbcsemse02_d.project.presentation.feedback.FeedbackViewModel
import dlbcsemse02_d.project.presentation.moderator.ModeratorViewModel
import dlbcsemse02_d.project.presentation.navigation.NavigationViewModel
import dlbcsemse02_d.project.presentation.playing.NowPlayingViewModel
import dlbcsemse02_d.project.presentation.playlist.PlaylistViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dataModule = module {
    single { MockDataStore() }
    single<SongRepository> { MockSongRepository(get()) }
    single<SongRequestRepository> { MockSongRequestRepository(get()) }
    single<ModeratorRepository> { MockModeratorRepository(get()) }
}

val applicationModule = module {
    single { SongService(get(), get()) }
    single { ModeratorService(get()) }
}

val viewModelModule = module {
    viewModelOf(::NowPlayingViewModel)
    viewModelOf(::PlaylistViewModel)
    viewModelOf(::ModeratorViewModel)
    viewModelOf(::FeedbackViewModel)
    viewModelOf(::NavigationViewModel)
}

val appModule = module {
    includes(dataModule, applicationModule, viewModelModule)
}
