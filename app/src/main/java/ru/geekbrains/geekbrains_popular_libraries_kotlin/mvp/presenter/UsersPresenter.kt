package ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.presenter

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.Scheduler
import moxy.MvpPresenter
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.entity.GithubUser
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.navigation.IScreens
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.model.repo.IGithubUsersRepo
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.presenter.list.IUserListPresenter
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.view.UsersView
import ru.geekbrains.geekbrains_popular_libraries_kotlin.mvp.view.list.UserItemView

class UsersPresenter(val uiScheduler: Scheduler, val usersRepo: IGithubUsersRepo, val router: Router, val screens: IScreens) : MvpPresenter<UsersView>() {
    class UsersListPresenter : IUserListPresenter {
        val users = mutableListOf<GithubUser>()
        override var itemClickListener: ((UserItemView) -> Unit)? = null

        override fun getCount() = users.size

        override fun bindView(view: UserItemView) {
            val user = users[view.pos]
            user.login?.let { view.setLogin(it) }
            user.avatarUrl?.let {view.loadAvatar(it)}
        }
    }

    val usersListPresenter = UsersListPresenter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadData()

        usersListPresenter.itemClickListener = { itemView ->
            val user = usersListPresenter.users[itemView.pos]
            router.navigateTo(screens.user(user))
        }
    }

    fun loadData() {
        usersRepo.getUsers()
            .observeOn(uiScheduler)
            .subscribe({ repos ->
                usersListPresenter.users.clear()
                usersListPresenter.users.addAll(repos)
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
