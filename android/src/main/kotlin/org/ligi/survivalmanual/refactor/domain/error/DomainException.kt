package domain.error

sealed class DomainException(message: String) : Exception(message) {
    data class ContentNotFoundException(override val message: String) : DomainException(message)
    data class NetworkException(override val message: String) : DomainException(message)
    data class PreferencesSaveException(override val message: String) : DomainException(message)
    data class UnknownErrorException(override val message: String = "An unknown error occurred") :
        DomainException(message)
}