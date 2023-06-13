package com.example.project_3.Fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.project_3.LoginActivity
import com.example.project_3.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.FirebaseDatabaseKtxRegistrar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException

class UserAccountFragment() :Fragment() {
    private lateinit var llLogout: LinearLayout
    private lateinit var llChangePassword: LinearLayout
    private lateinit var llInfoAccount: LinearLayout
    private lateinit var mview: View
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var imgAvatar: CircleImageView
    private val MY_REQUET_CODE=10
    private var uri: Uri? =null
    private lateinit var user:FirebaseUser








    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mview= inflater.inflate(R.layout.user_account_fragment,container,false)
        initUi()
        showUserInformation()
        val getImage=registerForActivityResult(
            ActivityResultContracts.GetContent(), ActivityResultCallback{
                imgAvatar.setImageURI(it)
            }
        )
        imgAvatar.setOnClickListener{
            getImage.launch("image/*")
        }
        initListener()

        return mview
    }
    private fun initUi() {
        llLogout=mview.findViewById(R.id.ll_logout)
        llChangePassword=mview.findViewById((R.id.ll_change_password))
        llInfoAccount = mview.findViewById(R.id.ll_info_account)
        tvName = mview.findViewById(R.id.tv_name)
        tvEmail = mview.findViewById(R.id.tv_email)
        imgAvatar = mview.findViewById(R.id.img_avatar)
    }
    private fun initListener() {
        llLogout.setOnClickListener {
            logout()
        }
        llChangePassword.setOnClickListener{
            changePassword()
        }
        llInfoAccount.setOnClickListener {
            infoAccount()
        }
//        imgAvatar.setOnClickListener {
//        changeImg()
//        }



    }

//    private fun changeImg() {
//        if (activity?.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            openGallery()
//        } else {
//            val permission =
//                arrayOf<String>(android.Manifest.permission.READ_EXTERNAL_STORAGE)//khai báo mảng truyền các permision yêu cầu
//
//            activity?.requestPermissions(permission, MY_REQUET_CODE)
//            //truyền vào 2 tham số là mẳng request permission (xin phep) và REQUET_CODE
//        }
//        openGallery()
//    }
//
//    private fun openGallery() {
//        val intent=Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent,MY_REQUET_CODE)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode==MY_REQUET_CODE&&requestCode == AppCompatActivity.RESULT_OK) {
//            uri = data?.data
//            val filepath:String="Photos/"+"userprofile"+user.uid
//            val reference=FirebaseStorage.getInstance().getReference(filepath)
//            reference.putFile(uri!!).addOnSuccessListener {
//                val task:Task<Uri> = it.metadata!!.reference!!.downloadUrl
//                task.addOnSuccessListener {
//                    val imgUrl=it.toString()
//                    val reference= FirebaseDatabase.getInstance().getReference("Users").child(user.uid)
//                    val hashMap:HashMap<String,Any> = HashMap()
//                    hashMap["imageUrl"] = imgUrl
//                    reference.updateChildren(hashMap)
//                }
//
//            }
//        }
//    }


    private fun showUserInformation() {
        val user = FirebaseAuth.getInstance().currentUser ?: return

        val name = user.displayName
        val email = user.email

        if (name == null) {
            tvName.visibility = View.GONE
        } else {
            tvName.visibility = View.VISIBLE
            tvName.text = name
        }


        tvEmail.text = email
        Glide.with(this).load(user.photoUrl).override(80, 80).error(R.drawable.ic_avatar_default)
            .into(imgAvatar)
    }
    private fun infoAccount() {

        val builder = AlertDialog.Builder(requireContext())
        val dialogInfoAccount = layoutInflater.inflate(R.layout.dialog_info_account, null)
        val edtName: EditText = dialogInfoAccount.findViewById(R.id.edt_name)
        val edtEmail: EditText = dialogInfoAccount.findViewById(R.id.edt_email)
        val btnUpdateInfo: Button = dialogInfoAccount.findViewById(R.id.btn_update_info)
        val edtPassword: EditText = dialogInfoAccount.findViewById(R.id.edt_password)
        builder.setView(dialogInfoAccount)
        val dialog: AlertDialog = builder.create()

        //set user infomation
        val user = FirebaseAuth.getInstance().currentUser ?: return//?là user ==null

        edtName.setText(user.displayName)
        edtEmail.setText(user.email)

        btnUpdateInfo.setOnClickListener {

            val profileUpdates = userProfileChangeRequest {
                val strNewName: String = edtName.text.toString().trim()
                displayName = strNewName


            }
            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        showUserInformation()
                    }

                }
            val strNewEmail: String = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(context, "Nhập mật khẩu hiện tại", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//            updateEmail(strNewEmail, password)
            val authCredential: AuthCredential =
                EmailAuthProvider.getCredential(user.email!!, password)
            user.reauthenticate(authCredential)
                .addOnSuccessListener {
                    user.updateEmail(strNewEmail)
                        .addOnSuccessListener {
                            //Toast.makeText(context, "Đã thay đổi mật khẩu", Toast.LENGTH_SHORT).show()
                            showUserInformation()
                            dialog.dismiss()
                            Toast.makeText(
                                context,
                                "Cập nhật thành công thông tin người dùng",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "" + it.message, Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "" + it.message, Toast.LENGTH_SHORT).show()
                }


        }
        dialog.show()


    }
    private fun logout() {
        Firebase.auth.signOut()
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun changePassword() {
        val builder= AlertDialog.Builder(requireContext())
        val dialogView=layoutInflater.inflate(R.layout.dialog_update_password,null)
        val edtCurrentPassword: EditText =dialogView.findViewById(R.id.edt_current_password)
        val edtChangePassword: EditText =dialogView.findViewById(R.id.edt_password_change)
        builder.setView(dialogView)
        val dialog: AlertDialog =builder.create()
        dialogView.findViewById<Button>(R.id.btnChange).setOnClickListener {
            val currentPassword=edtCurrentPassword.text.toString().trim()
            val newPassword=edtChangePassword.text.toString().trim()
            if (TextUtils.isEmpty(currentPassword)){
                Toast.makeText(context, "Nhập mật khẩu hiện tại", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (newPassword.length<6){
                Toast.makeText(context, "Mật khẩu phải ít nhất 6 kí tự", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            updatePassword(currentPassword,newPassword)
            dialog.dismiss()

        }
        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updatePassword(currentPassword: String, newPassword: String) {
        val user = Firebase.auth.currentUser
        val authCredential: AuthCredential =
            EmailAuthProvider.getCredential(user?.email!!,currentPassword)
        user.reauthenticate(authCredential)
            .addOnSuccessListener {
                user.updatePassword(newPassword)
                    .addOnSuccessListener{
                        Toast.makeText(context, "Đã thay đổi mật khẩu", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, ""+it.message, Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, ""+it.message, Toast.LENGTH_SHORT).show()
            }



    }
}


