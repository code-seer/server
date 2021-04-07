package io.learnet.account.data.entity

import org.hibernate.annotations.Type
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(schema = "public", name = "demo_user")
open class DemoUserEntity: Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", columnDefinition = "bigserial")
        open var id: Long? = null

        @Basic
        @Column(name = "first_name", columnDefinition = "text")
        open var firstName: String? = null

        @Basic
        @Column(name = "last_name", columnDefinition = "text")
        open var lastName: String? = null

        @Basic
        @Column(name = "city", columnDefinition = "text")
        open var city: String? = null

        @Basic
        @Column(name = "state", columnDefinition = "text")
        open var state: String? = null

        @Basic
        @Column(name = "zip", columnDefinition = "text")
        open var zip: String? = null

        @Basic
        @Column(name = "address", columnDefinition = "text")
        open var address: String? = null

        
        @Type(type = "io.learnet.account.data.type.ArrayUserType")
        @Column(name = "favorites", columnDefinition = "text[]")
        open var favorites: Array<String> = emptyArray()

        @Basic
        @Column(name = "avatar")
        open var avatar: String? = null

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
