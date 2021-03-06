package shop.domains

import java.util.UUID
import javax.crypto.Cipher

import scala.util.control.NoStackTrace

import shop.infrastructure.optics.uuid

import derevo.cats._
import derevo.circe.magnolia.{ decoder, encoder }
import derevo.derive
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Decoder
import io.circe.refined._
import io.estatico.newtype.macros.newtype

object auth {

  @derive(decoder, encoder, show, uuid, eqv)
  @newtype case class UserId(value: UUID)
  @newtype case class JwtToken(value: String)
  @derive(decoder, encoder, show, eqv)
  @newtype
  case class UserName(value: String)

  @derive(decoder, encoder, eqv, show)
  @newtype
  case class Password(value: String)

  @derive(decoder, encoder, eqv, show)
  @newtype case class EncrytedPassword(value: String)
  @derive(decoder, encoder)
  @newtype
  case class UserNameParam(value: NonEmptyString) {
    def toDomain: UserName = UserName(value.toLowerCase)
  }

  @derive(decoder, encoder)
  @newtype
  case class PasswordParam(value: NonEmptyString) {
    def toDomain: Password = Password(value)
  }

  @derive(decoder, encoder, eqv, show)
  @newtype
  case class EncryptedPassword(value: String)

  @newtype
  case class EncryptCipher(value: Cipher)

  @newtype
  case class DecryptCipher(value: Cipher)

  case class UserNotFound(username: UserName)    extends NoStackTrace
  case class UserNameInUse(username: UserName)   extends NoStackTrace
  case class InvalidPassword(username: UserName) extends NoStackTrace
  case object UnsupportedOperation               extends NoStackTrace

  @derive(decoder, encoder)
  case class CreateUser(
      username: UserNameParam,
      password: PasswordParam
  )

  @derive(decoder, encoder)
  case class LoginUser(
      username: UserNameParam,
      password: PasswordParam
  )

  @newtype
  case class ClaimContent(uuid: UUID)

  object ClaimContent {
    implicit val jsonDecoder: Decoder[ClaimContent] =
      Decoder.forProduct1("uuid")(ClaimContent.apply)
  }

}
