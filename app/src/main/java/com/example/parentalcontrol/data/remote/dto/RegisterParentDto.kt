
data class RegisterParentRequest(
    val name: String,
    val email: String,
    val password: String,
//    val password_confirmation: String
)

data class RegisterParentResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: Int,
    val name: String,
    val email: String
)

