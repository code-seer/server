package io.learnet.account.data.repo

import io.learnet.account.data.entity.SecurityEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface SecurityRepo : JpaRepository<SecurityEntity, Long>
