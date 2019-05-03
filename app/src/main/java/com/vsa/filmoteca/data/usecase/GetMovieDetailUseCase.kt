package com.vsa.filmoteca.data.usecase

import com.vsa.filmoteca.data.repository.MoviesDataRepository

import rx.Observable
import javax.inject.Inject

/**
 * Created by albertovecinasanchez on 18/7/16.
 */

class GetMovieDetailUseCase @Inject constructor(private val repository: MoviesDataRepository) {

    fun movieDetail(url: String): Observable<String> {
        return repository.movieDetail(url)
    }

}
