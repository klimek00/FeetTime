package com.example.firebaseforum.ui.login

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebaseforum.R
import com.example.firebaseforum.data.User
import com.example.firebaseforum.databinding.FragmentLoginBinding
import com.example.firebaseforum.firebase.FirebaseHandler
import com.example.firebaseforum.helpers.KeyboardHelper
import com.example.firebaseforum.helpers.MyTextWatcher
import com.example.firebaseforum.helpers.TimerTaskListener
import com.google.android.material.snackbar.Snackbar


class LoginRegisterFragment : Fragment() {
  private var passwordConfirmValid: Boolean = false
  private var passwordValid: Boolean = false
  private var usernameValid: Boolean = false
  // Flag indicating the state of the form. True means that the form is used for Login,
  // false means that the form is used to register user
  private var isLogin = true

  private var _binding: FragmentLoginBinding? = null


  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    isLogin = true


    binding.loginPasswordConfirm.visibility = View.GONE
    binding.loginRegisterButton.isEnabled = false


    setupButtons()
//    setupEditText()
  }

  private val binding get() = _binding!!
  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onStart() {
    super.onStart()
    if (FirebaseHandler.Authentication.isLoggedIn())
      findNavController().navigate(R.id.action_navigation_login_to_navigation_home)
  }

  override fun onPause() {
    super.onPause()
    cancelTimers()
  }

  override fun onResume() {
    super.onResume()
    setupEditTexts()
  }

  // This function sets up the buttons in the login/register screen
  private fun setupButtons() {
    // Set a click listener to the login/register toggle button
    binding.loginRegisterToggle.setOnClickListener {
    // Toggle the screen to login or register mode
      toggleLoginRegister()
    }
    binding.loginRegisterButton.setOnClickListener {
      cancelTimers() // Cancel any existing timers
      // Hide software keyboard after button press
      KeyboardHelper.hideSoftwareKeyboard(requireContext(), binding.root.windowToken)
      if (validate()) {
      // get the email and possword from the form
        val email = binding.loginUsername.editText?.text.toString()
        val password = binding.loginPassword.editText?.text.toString()
        if (isLogin)
          login(email, password) // If the fragment is in login state perform login
        else register(email, password) // perform register otherwise
      }
    }
  }

  private fun login(email: String, password: String) {
    //call the firebase auth login method
    FirebaseHandler.Authentication.login(email, password).apply {
      // if successful
      addOnSuccessListener {
        Log.d("login", "signInWithEmail: success")
        findNavController().navigate(R.id.action_navigation_login_to_navigation_home)
      }
      addOnFailureListener {
        Log.w("login", "signinemail: failure", it)
        Snackbar.make(
          binding.root, "Auth failed",
          Snackbar.LENGTH_SHORT
        ).show()
      }
    }
  }

  private fun register(email: String, password: String) {
    //call firebase auth register method
    FirebaseHandler.Authentication.register(email, password).apply {
      addOnSuccessListener {
        Log.d("register", "createUserWithEmail: success")
        FirebaseHandler.RealtimeDatabase.addUser(User(email))
        //val action = LoginRegisterFragmentDirections.actionNavigationLoginToEditProfileFragment(true)
        val userUid = FirebaseHandler.Authentication.getUserUid()
        if (userUid != null){
          FirebaseHandler.RealtimeDatabase.setUserMoney(userUid,0)
          FirebaseHandler.RealtimeDatabase.setUserMoney(userUid,0)
        }

        val action = LoginRegisterFragmentDirections.actionNavigationLoginToEditProfileFragment(firstRegister = true)
        findNavController().navigate(action)
      }
      addOnFailureListener {
        Log.w("register", "createUserWithEmail: failure", it)
        Snackbar.make(
          binding.root, "Registering failed",
          Snackbar.LENGTH_SHORT
        ).show()
      }
    }
  }

  private fun toggleLoginRegister() {
    // Toggle the isLogin flag
    isLogin = !isLogin
    if (isLogin) {
      // When the isLogin is true:
      // Change the text on the login/register button to login
      binding.loginRegisterButton.setText(R.string.login_str)
      // Change the text on the login/register toggle button to register
      binding.loginRegisterToggle.setText(R.string.register_str)
      // Hide the password confirm field
      binding.loginPasswordConfirm.visibility = View.GONE
    } else {
      // When the isLogin is false:
      // Change the text on the login/register button to register
      binding.loginRegisterButton.setText(R.string.register_str)
      // Change the text on the login/register toggle button to login
      binding.loginRegisterToggle.setText(R.string.login_str)
      // Show the password confirm field
      binding.loginPasswordConfirm.visibility = View.VISIBLE
    }
    // Change the state of the login/register button depending on the state of the fields flags
    binding.loginRegisterButton.isEnabled = validate()
  }


/*
  /// VALIDATION ///
*/
  private fun validate(): Boolean {
    return if (isLogin)
      passwordValid && usernameValid
    else
      passwordValid && usernameValid && passwordConfirmValid
  }

  private fun checkUserName(username: String) {
    Log.i("checkUserName", username)
    if (!isUserNameValid(username)) {
      binding.loginUsername.error = getString(R.string.invalid_username)
      usernameValid = false
    } else {
      binding.loginUsername.error = ""
      usernameValid = true
    }
  }

  private fun isUserNameValid(username: String): Boolean {
    return if (username.contains("@")) {
      Patterns.EMAIL_ADDRESS.matcher(username).matches()
    } else {
      false
    }
  }
  private fun checkPassword(password: String) {
    Log.i("checkPassword", password)
    if (!isPasswordValid(password)) {
      binding.loginPassword.error = getString(R.string.invalid_password)
      passwordValid = false
    } else {
      binding.loginPassword.error = ""
      passwordValid = true
    }
  }
  private fun isPasswordValid(password: String): Boolean {
    return password.length >= 6
  }

  private fun checkRegisterPassword(passwordConfirm: String, password: String) {
    Log.i("checkRegisterPassword", "$passwordConfirm $password")
    if (!isPasswordConfirmValid(passwordConfirm, password)) {
      binding.loginPasswordConfirm.error = getString(R.string.password_mismatch)
      passwordConfirmValid = false
    } else {
      true
      binding.loginPasswordConfirm.error = ""
      passwordConfirmValid = true
    }
  }
  private fun isPasswordConfirmValid(passwordConfirm: String, password: String): Boolean {
    return passwordConfirm == password
  }

  /// END OF VALIDATION ///

  //********************************************************************************************//
//Watchers for input fields - trigger validation after some delay (500 ms is default)
//This code defines text watchers for input fields (password, password confirmation, and username)
//to trigger validation after a delay of 500 ms. The MyTextWatcher class takes a TimerTaskListener
//object that implements the timerRun() function to execute the validation logic after the
//specified delay.
//********************************************************************************************//
  // Watcher for password field
  private val passwordTextWatcher: MyTextWatcher = MyTextWatcher(object : TimerTaskListener {
    override fun timerRun() {
      binding.loginPassword.post {
        // Get the current input from the password field
        val passwordInput = binding.loginPassword.editText?.text.toString()
        if (passwordInput.isNotEmpty()) {
          // Call checkPassword function to validate the password
          checkPassword(passwordInput)
          // Enable or disable the register button based on validation result
          binding.loginRegisterButton.isEnabled = validate()
        } else {
          // If the password field is empty, disable the register button
          passwordValid = false
          binding.loginRegisterButton.isEnabled = false
        }
      }
    }
  })
  // Watcher for password confirmation field
  private val passwordConfirmTextWatcher: MyTextWatcher =
    MyTextWatcher(object : TimerTaskListener {
      override fun timerRun() {
        binding.loginPasswordConfirm.post {
          // Get the current input from the password confirmation field
          val passwordConfirm = binding.loginPasswordConfirm.editText?.text.toString()
          // Get the current input from the password field
          val password = binding.loginPassword.editText?.text.toString()
          if (passwordConfirm.isNotEmpty() && password.isNotEmpty()) {
            // Call checkRegisterPassword function to validate the password confirmation
            checkRegisterPassword(passwordConfirm, password)
            // Enable or disable the register button based on validation result
            binding.loginRegisterButton.isEnabled = validate()
          } else {
            // If either the password or the password confirmation field is empty,
            // disable the register button
            passwordConfirmValid = false
            binding.loginRegisterButton.isEnabled = false
          }
        }
      }
    })
  // Watcher for username field
  private val usernameTextWatcher: MyTextWatcher = MyTextWatcher(object : TimerTaskListener {
    override fun timerRun() {
      binding.loginUsername.post {
        // Get the current input from the username field
        val username = binding.loginUsername.editText?.text.toString()
        if (username.isNotEmpty()) {
          // Call checkUserName function to validate the username
          checkUserName(username)
          // Enable or disable the register button based on validation result
          binding.loginRegisterButton.isEnabled = validate()
        } else {
          // If the username field is empty, disable the register button
          usernameValid = false
          binding.loginRegisterButton.isEnabled = false
        }
      }
    }
  })
  // Cancel all timers used by the text watchers
  private fun cancelTimers() {
    usernameTextWatcher.cancelTimer()
    passwordTextWatcher.cancelTimer()
    passwordConfirmTextWatcher.cancelTimer()
    binding.loginUsername.editText?.removeTextChangedListener(usernameTextWatcher)
    binding.loginPassword.editText?.removeTextChangedListener(passwordTextWatcher)
    binding.loginPasswordConfirm.editText?.removeTextChangedListener(passwordConfirmTextWatcher)
  }

  private fun setupEditTexts() {
    binding.loginUsername.editText?.addTextChangedListener(usernameTextWatcher)
    binding.loginPassword.editText?.addTextChangedListener(passwordTextWatcher)
    binding.loginPasswordConfirm.editText?.addTextChangedListener(passwordConfirmTextWatcher)
  }
}
