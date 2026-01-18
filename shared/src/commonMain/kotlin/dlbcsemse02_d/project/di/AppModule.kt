package dlbcsemse02_d.project.di

import dlbcsemse02_d.project.application.service.SongService
import dlbcsemse02_d.project.domain.repository.SongRepository
import dlbcsemse02_d.project.infrastructure.repository.MockSongRepository
import dlbcsemse02_d.project.presentation.playing.NowPlayingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single<SongRepository>(named("mock")) {
        MockSongRepository()
    }

    single<SongRepository> {
        get<SongRepository>(named("mock"))
    }
}

val applicationModule = module {
    single { SongService(get()) }
}

val viewModelModule = module {
    viewModelOf(::NowPlayingViewModel)
}

val appModule = module {
    includes(dataModule, applicationModule, viewModelModule)
}
