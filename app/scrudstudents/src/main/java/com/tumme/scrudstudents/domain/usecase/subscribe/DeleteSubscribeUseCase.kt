package com.tumme.scrudstudents.domain.usecase.subscribe

import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository

class DeleteSubscribeUseCase(private val repo: SCRUDRepository){
    /*
      The `invoke` operator allows the class instance to be called as if it were a function.
      It's a suspend function to ensure that the underlying database operation
      does not block the main thread.
      @param subscribe The SubscribeEntity object to be deleted.
     */
     suspend operator fun invoke(subscribe: SubscribeEntity) = repo.deleteSubscribe(subscribe)

}