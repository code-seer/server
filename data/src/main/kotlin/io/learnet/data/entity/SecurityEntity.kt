package io.learnet.data.entity

import org.hibernate.annotations.Type
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(schema = "public", name = "security")
open class SecurityEntity: Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", columnDefinition = "bigserial")
        open var id: Long? = null

        @OneToOne(mappedBy = "security")
        open var userProfile: UserProfileEntity? = null

        @Type(type = "io.learnet.data.type.ArrayUserType")
        @Column(name = "roles", columnDefinition = "text[]")
        open var roles: Array<String> = emptyArray()

        @Basic
        @Column(name = "created_dt", columnDefinition = "timestamptz")
        open var createdDt: OffsetDateTime = OffsetDateTime.now()

        @Basic
        @Column(name = "updated_dt", columnDefinition = "timestamptz")
        open var updatedDt: OffsetDateTime = OffsetDateTime.now()

        @Basic
        @Column(name = "_uuid", columnDefinition = "uuid")
        open var uuid: UUID? = null
}
