package ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.presenter

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.Scheduler
import moxy.MvpPresenter
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.entity.GithubRepository
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.entity.GithubUser
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.navigation.IScreens
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.repo.IGithubRepositoriesRepo
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.presenter.list.IRepositoryListPresenter
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.view.UserView
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.view.list.RepositoryItemView

class UserPresenter(val uiScheduler: Scheduler, val repositoriesRepo: IGithubRepositoriesRepo, val router: Router, val user: GithubUser, val screens: IScreens) : MvpPresenter<UserView>() {

    class RepositoriesListPresenter : IRepositoryListPresenter {
        val repositories = mutableListOf<GithubRepository>()
        override var itemClickListener: ((RepositoryItemView) -> Unit)? = null
        override fun getCount() = repositories.size

        override fun bindView(view: RepositoryItemView) {
            val repository = repositories[view.pos]
            repository.name?.let { view.setName(it) }
        }
    }

    val repositoriesListPresenter = RepositoriesListPresenter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadData()

        repositoriesListPresenter.itemClickListener = { itemView ->
            val repository = repositoriesListPresenter.repositories[itemView.pos]
            router.navigateTo(screens.repository(repository))
        }
    }

    fun loadData() {
        repositoriesRepo.getRepositories(user)
            .observeOn(uiScheduler)
            .subscribe({ repositories ->
                repositoriesListPresenter.repositories.clear()
                repositoriesListPresenter.repositories.addAll(repositories)
                viewState.updateList()
            }, {
                println("Error: ${it.message}")
            })
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}
