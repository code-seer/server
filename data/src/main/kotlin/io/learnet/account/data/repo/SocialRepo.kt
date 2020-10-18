package io.learnet.account.data.repo

import io.learnet.account.data.entity.SocialEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface SocialRepo : JpaRepository<SocialEntity, Long>
