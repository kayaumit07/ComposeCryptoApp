package com.example.cryptoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptoapp.model.CryptoModel
import com.example.cryptoapp.service.CryptoAPI
import com.example.cryptoapp.ui.theme.CryptoAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private val BASE_URL="https://api.wazirx.com/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoAppTheme {
              MainView()
            }
        }
    }
}


@Composable
fun MainView(){
    val BASE_URL="https://api.wazirx.com/"
    val cryptoModels= remember { mutableStateListOf<CryptoModel>() }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoAPI::class.java)


    val call=retrofit.getData()
    call.enqueue(object :Callback<List<CryptoModel>>{
        override fun onResponse(
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>
        ) {
            if (response.isSuccessful)
            {
                response.body()?.let {
                    it.forEach(){ it2->
                        if (it2.quoteAsset=="usdt")
                        {
                            cryptoModels.add(it2)
                        }
                    }
                }
            }


        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }

    })


    Scaffold(topBar = {AppBar()},modifier = Modifier.fillMaxSize()) {
        CryptoList(cryptos = cryptoModels)
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CryptoAppTheme {
        MainView()
    }
}

@Composable
fun AppBar(){
    TopAppBar(contentPadding = PaddingValues(10.dp)) {
        Text(text = "UK Crypto App", fontSize = 25.sp)
    }
}

@Composable
fun CryptoList(cryptos :List<CryptoModel>){
  LazyColumn(contentPadding = PaddingValues(5.dp)){
      items(cryptos){ crypto->
          CryptoRow(crypto=crypto)
      }
  }
}

@Composable
fun CryptoRow(crypto:CryptoModel){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.surface)) {

        Row() {
            Text(text = crypto.baseAsset,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(2.dp),
                fontWeight = FontWeight.Bold
            )
            Text(text = " | "+ crypto.quoteAsset,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(2.dp),
                fontWeight = FontWeight.Bold
            )
        }

        Text(text = crypto.bidPrice,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(2.dp)
            )
    }
}