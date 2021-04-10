package io.learnet.data.entity

import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(schema = "public", name = "user_settings")
open class UserSettingsEntity: Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", columnDefinition = "bigserial")
        open var id: Long? = null

        @OneToOne(mappedBy = "userSettings")
        open var userProfile: UserProfileEntity? = null

        @Basic
        @Column(name = "timezone", columnDefinition = "text")
        open var timezone: String? = null

        @Basic
        @Column(name = "language", columnDefinition = "text")
        open var language: String? = null

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
