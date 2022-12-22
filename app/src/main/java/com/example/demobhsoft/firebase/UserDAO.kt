package com.example.demobhsoft.firebase

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.demobhsoft.datalocal.MySharedPreferences
import com.example.demobhsoft.model.UserModel
import com.example.demobhsoft.screen.LoginActivity
import com.example.demobhsoft.screen.MainActivity.MainActivity
import com.example.demobhsoft.utils.Constant
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserDAO {

    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val TAG = "demobhsoft"
    private val mySharedPreferences = MySharedPreferences()

    fun createUserWithEmail(user: UserModel, activity: Activity){
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    user.fireBaseId = currentUser?.uid.toString()
                    Log.d(TAG, "user firebase id: ${user.fireBaseId}")
                    // Sign in success, update UI with the signed-in user's information
                        db.collection(Constant.USER.TB_USER)
                        .document(user.userId.toString())
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(activity, "Register success", Toast.LENGTH_SHORT).show()
                            activity.startActivity(Intent(activity, LoginActivity::class.java))
                        }
                        .addOnFailureListener{
                            Toast.makeText(activity, "Register failure: $it", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "createUserWithEmail: $it")
                        }
                    Log.d(TAG, "createUserWithEmail:success")

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun getListUser(list: (ArrayList<UserModel>) -> Unit){
        var listUser = ArrayList<UserModel>()
        db.collection(Constant.USER.TB_USER)
            .get()
            .addOnSuccessListener { task ->
                listUser.clear()
                for (document in task) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val user: UserModel = document.toObject(UserModel::class.java)
                    listUser.add(user)
                    list(listUser)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
        Log.d(TAG, "getListUser: listUser: ${listUser.size}")
    }

//    fun getUser(userId: String): UserModel{
//        var user: UserModel
//        db.collection(Constant.USER.TB_USER).document(userId)
//            .get()
//            .addOnSuccessListener { result ->
//               if (result != null) {
//                   Log.d(TAG, "getUser: ${result.data}")
////                   return result.data
////                   user = result.data
//
//               }
//                else{
//
//               }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "Error getting documents: ", exception)
////                return UserModel()
//            }
//    }

    fun loginUser(email: String, password: String, listUser: ArrayList<UserModel>, activity: Activity){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    for (userModel: UserModel in listUser){
                        if (userModel.fireBaseId.equals(user?.uid.toString())){
                            mySharedPreferences.setModel(activity, userModel)
                            Log.d(TAG, "USERCURRENT: ${user?.uid} ===== USERMODEL ${userModel.fireBaseId}" )
                        }
                    }

                    Toast.makeText(activity, "Login success.", Toast.LENGTH_SHORT).show();
                    activity.startActivity(Intent(activity, MainActivity::class.java))

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}