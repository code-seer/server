package io.learnet.account.data.entity

import org.hibernate.search.engine.backend.types.Projectable
import org.hibernate.search.engine.backend.types.Sortable
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*
import java.io.Serializable
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(schema = "public", name = "user_profile")
open class UserProfileEntity: Serializable {

        @Id
        @Column(name = "id", columnDefinition = "bigserial")
        open var id: Long? = null

        @OneToOne
        open var address: AddressEntity? = null

        @OneToOne
        open var social: SocialEntity? = null

        @OneToOne
        open var security: SecurityEntity? = null

        @OneToOne
        open var userSettings: UserSettingsEntity? = null

        @Basic
        @Column(name = "first_name", columnDefinition = "text")
        open var firstName: String? = null

        @Basic
        @Column(name = "last_name", columnDefinition = "text")
        open var lastName: String? = null

        @Basic
        @Column(name = "middle_name", columnDefinition = "text")
        open var middleName: String? = null

        @Basic
        @Column(name = "title", columnDefinition = "text")
        open var title: String? = null

        @Basic
        @Column(name = "gender", columnDefinition = "text")
        open var gender: String? = null

        @Basic
        @Column(name = "email", columnDefinition = "text")
        open var email: String? = null

        @Basic
        @Column(name = "is_new_user", columnDefinition = "text")
        open var isNewUser: Boolean = true

        @Basic
        @Column(name = "home_phone", columnDefinition = "text")
        open var homePhone: String? = null

        @Basic
        @Column(name = "mobile_phone", columnDefinition = "text")
        open var mobilePhone: String? = null

        @Basic
        @Column(name = "date_of_birth", columnDefinition = "date")
        open var dateOfBirth: LocalDate? = null

        @Basic
        @Column(name = "avatar")
        open var avatar: String? = null

        @Basic
        @Column(name = "avatar_object_key")
        open var avatarObjectKey: String? = null

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
