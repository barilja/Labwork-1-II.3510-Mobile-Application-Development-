package com.tumme.scrudstudents.domain.usecase.subscribe

import com.tumme.scrudstudents.data.repository.SCRUDRepository

class GetSubscribeUseCase(private val repo: SCRUDRepository){
    /*
      The `invoke` operator allows the class instance to be called as a function.
      This function directly calls the repository method to get all subscriptions.
      It returns a Flow, which allows the UI to observe the subscription list for changes in real-time.
      @return A Flow that emits a list of SubscribeEntity objects.
     */
    operator fun invoke() = repo.getAllSubscribes()
}