package com.example.task.android.Screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson


//@Composable
//fun voting( navController: NavHostController) {
//    val context = LocalContext.current
//    val db = FirebaseFirestore.getInstance()
//    val userRef = db.collection("Vote")
//        .document("Cast_vote")
//
//    val radioOptions = listOf("Candidate A", "Candidate B", "Candidate C", "Candidate D")
//    val  mAuth = FirebaseAuth.getInstance()
//
//    var selectedCandidate by remember { mutableStateOf("") }
//
//    val voteList = get_votes(context)
//
//    voteList?.get(2)?.let { Log.d("Check" , it.toString()) }
//
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        Text("Voting")
//
//        Spacer(modifier = Modifier.padding(20.dp))
//
//        LazyColumn {
//            items(radioOptions) { candidate ->
//                RadioButton(
//                    text = candidate,
//                    isSelected = candidate == selectedCandidate,
//                    onOptionSelected = { selectedCandidate = it }
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.padding(20.dp))
//
//        Button(onClick = {
//            updateVote(context, userRef, selectedCandidate,navController)
//        }) {
//            Text(text = "VOTE")
//        }
//    }
//}
//
//
//private fun updateVote(
//    context:Context,
//    userRef: DocumentReference,
//    selectedCandidate: String,
//    navController: NavHostController
//) {
//    userRef.get()
//        .addOnSuccessListener { document ->
//            val currentCount = document.getDouble(selectedCandidate)?.toInt() ?: 0
//            userRef.update(selectedCandidate, currentCount + 1)
//                .addOnSuccessListener {
//                    Toast.makeText(context, "Vote recorded successfully", Toast.LENGTH_SHORT).show()
//                    navController.navigate("Voted Successfully")
//                }
//                .addOnFailureListener { e ->
//                    Toast.makeText(context, "Error recording vote: $e", Toast.LENGTH_SHORT).show()
//                }
//        }
//        .addOnFailureListener { e ->
//            Toast.makeText(context, "Error recording vote: $e", Toast.LENGTH_SHORT).show()
//        }
//
//
//
//}



@Composable
fun voting(navController: NavHostController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val userRef = db.collection("Vote")
        .document("Cast_vote")
    val votersRef = db.collection("Voters")

    val radioOptions = listOf("Candidate A", "Candidate B", "Candidate C", "Candidate D")
    val mAuth = FirebaseAuth.getInstance()

    var selectedCandidate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Voting")

        Spacer(modifier = Modifier.padding(20.dp))

        LazyColumn {
            items(radioOptions) { candidate ->
                RadioButton(
                    text = candidate,
                    isSelected = candidate == selectedCandidate,
                    onOptionSelected = { selectedCandidate = it }
                )
            }
        }

        Spacer(modifier = Modifier.padding(20.dp))

        Button(onClick = {
            val userId = mAuth.currentUser?.uid
            if (userId != null) {
                votersRef.document(userId).get()
                    .addOnSuccessListener { document ->
                        if (!document.exists()) {
                            updateVote(context, userRef, selectedCandidate, navController, userId)
                            votersRef.document(userId).set(mapOf("voted" to true))
                        } else {
                            Toast.makeText(context, "You have already voted", Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error checking voter status: $e", Toast.LENGTH_SHORT).show()
                    }
            }
        }) {
            Text(text = "VOTE")
        }
    }
}

private fun updateVote(
    context: Context,
    userRef: DocumentReference,
    selectedCandidate: String,
    navController: NavHostController,
    userId: String
) {
    userRef.get()
        .addOnSuccessListener { document ->
            val currentCount = document.getDouble(selectedCandidate)?.toInt() ?: 0
            userRef.update(selectedCandidate, currentCount + 1)
                .addOnSuccessListener {
                    Toast.makeText(context, "Vote recorded successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate("Voted Successfully")
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error recording vote: $e", Toast.LENGTH_SHORT).show()
                }
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error recording vote: $e", Toast.LENGTH_SHORT).show()
        }
}
@Composable
fun RadioButton(
    text: String,
    isSelected: Boolean,
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOptionSelected(text) }
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.RadioButton(
            selected = isSelected,
            onClick = { onOptionSelected(text) }
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}


// Save the JSON string to SharedPreferences
fun save_votes(context: Context,key:String) {
    val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putString("votes", key)
    editor.apply()
}

fun get_votes(context: Context): String? {
    val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    return sharedPref.getString("votes", null)
}




data class IsvoteData(
    val username:String,
    val isvoted:Boolean
)

