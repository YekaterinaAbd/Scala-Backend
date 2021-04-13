trait Validator[T] {
  def validate(t: T): Option[ApiError]
}

object CreateTodoValidator extends Validator[CreateTodo] {
  def validate(createTodo: CreateTodo): Option[ApiError] = {
    if (createTodo.title.isEmpty) Some(ApiError.emptyTitleField)
    else if (createTodo.description.isEmpty) Some(ApiError.emptyDescriptionField)
    else None
  }
}

object UpdateTodoValidator extends Validator[UpdateTodo] {
  def validate(updateTodo: UpdateTodo): Option[ApiError] = {
    if (updateTodo.id.isEmpty) Some(ApiError.emptyIdField)
    else None
  }
}

