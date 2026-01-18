package dlbcsemse02_d.project.di

import dlbcsemse02_d.project.application.service.SongRequestService
import dlbcsemse02_d.project.application.service.SongService
import dlbcsemse02_d.project.domain.repository.SongRepository
import dlbcsemse02_d.project.domain.repository.SongRequestRepository
import dlbcsemse02_d.project.infrastructure.repository.MockSongRepository
import dlbcsemse02_d.project.infrastructure.repository.MockSongRequestRepository
import dlbcsemse02_d.project.presentation.feedback.FeedbackViewModel
import dlbcsemse02_d.project.presentation.playing.NowPlayingViewModel
import dlbcsemse02_d.project.presentation.playlist.PlaylistViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dataModule = module {
    single<SongRepository> { MockSongRepository() }
    single<SongRequestRepository> { MockSongRequestRepository() }
}

val applicationModule = module {
    single { SongService(get()) }
    single { SongRequestService(get()) }
}

val viewModelModule = module {
    viewModelOf(::NowPlayingViewModel)
    viewModelOf(::PlaylistViewModel)
    viewModelOf(::FeedbackViewModel)
}

val appModule = module {
    includes(dataModule, applicationModule, viewModelModule)
}
