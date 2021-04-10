package io.learnet.data.repo

import io.learnet.data.entity.AddressEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface AddressRepo : JpaRepository<AddressEntity, Long>
