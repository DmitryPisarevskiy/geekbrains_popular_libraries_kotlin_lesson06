package ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.navigation

import com.github.terrakok.cicerone.Screen
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.entity.GithubRepository
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.entity.GithubUser

interface IScreens {
    fun users(): Screen
    fun user(githubUser: GithubUser): Screen
    fun repository(githubRepository: GithubRepository): Screen
}