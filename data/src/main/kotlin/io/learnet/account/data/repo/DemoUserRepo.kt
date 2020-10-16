package io.learnet.account.data.repo

import io.learnet.account.data.entity.DemoUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional
interface DemoUserRepo : JpaRepository<DemoUserEntity, Long> {
    fun deleteByUuid(uuid: UUID)
}
