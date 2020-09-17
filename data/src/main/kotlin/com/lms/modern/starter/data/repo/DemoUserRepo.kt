package com.lms.modern.starter.data.repo

import com.lms.modern.starter.data.entity.DemoUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional
interface DemoUserRepo : JpaRepository<DemoUserEntity, Long> {
    fun deleteByUuid(uuid: UUID)
}
