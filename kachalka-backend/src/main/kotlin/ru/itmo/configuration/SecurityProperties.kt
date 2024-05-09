package ru.itmo.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import ru.itmo.model.UserPermission
import ru.itmo.model.UserRole

@ConfigurationProperties("security")
@ConstructorBinding
data class SecurityProperties(
    val permissions: Map<UserRole, List<UserPermission>>,
)
