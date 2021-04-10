package io.learnet.data.repo

import io.learnet.data.entity.UserProfileEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface UserProfileRepo : JpaRepository<UserProfileEntity, Long> {
    fun findByEmail(email: String): UserProfileEntity?
}
