import akka.http.scaladsl.model.{StatusCode, StatusCodes}

final case class ApiError private(statusCode: StatusCode, message: String)

object ApiError {
  private def apply(statusCode: StatusCode, message: String): ApiError =
    new ApiError(statusCode, message)

  val generic: ApiError = new ApiError(StatusCodes.InternalServerError, "Unknown error.")
  val emptyIdField: ApiError = new ApiError(StatusCodes.BadRequest, "Empty ID field.")
  val emptyTitleField: ApiError = new ApiError(StatusCodes.BadRequest, "Empty title.")
  val emptyDescriptionField: ApiError = new ApiError(StatusCodes.BadRequest, "Empty description.")
  val emptyStatusField: ApiError = new ApiError(StatusCodes.BadRequest, "Empty status.")
  val duplicateTitleField: ApiError = new ApiError(StatusCodes.BadRequest, "Title already exists.")
  val toDoNotFound: ApiError = new ApiError(StatusCodes.NotFound, "ToDo with this ID is not found.")
}
