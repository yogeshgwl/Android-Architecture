package com.task.ui.component.login

/**
 * This file represent the Input events on the screen.
 */
sealed class UserInputEvent {
    class Password(val input: String) : UserInputEvent()
    class Email(val input: String) : UserInputEvent()
}
