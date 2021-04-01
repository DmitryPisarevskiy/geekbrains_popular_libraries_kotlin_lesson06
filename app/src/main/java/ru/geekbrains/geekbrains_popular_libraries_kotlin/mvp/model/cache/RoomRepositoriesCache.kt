package ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.cache

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.entity.GithubRepository
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.entity.GithubUser
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.entity.room.RoomGithubRepository
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.entity.room.db.Database
import java.lang.RuntimeException

class RoomRepositoriesCache(val db:Database): IRepositoriesCache {
    override fun getRepositories(user: GithubUser) = Single.fromCallable {
        val roomUser = db.userDao.findByLogin(user.login) ?: throw RuntimeException("No user in DB")
        db.repositoryDao.findForUser(roomUser.id).map { roomRepo ->
            GithubRepository(roomRepo.id, roomRepo.name, roomRepo.forksCount)
        }
    }

    override fun putRepositories(user: GithubUser, repositories: List<GithubRepository>) = Completable.fromAction {
        val roomUser = db.userDao.findByLogin(user.login) ?: throw RuntimeException("No user in DB")
        val roomRepos = repositories.map { repo ->
            RoomGithubRepository(repo.id, repo.name, repo.forksCount, roomUser.id)
        }
        db.repositoryDao.insert(roomRepos)
    }
}