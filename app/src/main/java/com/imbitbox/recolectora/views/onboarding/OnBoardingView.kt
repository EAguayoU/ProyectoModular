package com.imbitbox.recolectora.views.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.imbitbox.recolectora.R
import com.imbitbox.recolectora.components.LoaderData
import com.imbitbox.recolectora.models.onBoarding.clPageData
import com.imbitbox.recolectora.models.tools.clDataStore
import com.imbitbox.recolectora.ui.theme.clrAzul
import com.imbitbox.recolectora.ui.theme.clrGrisOscuro
import com.imbitbox.recolectora.ui.theme.clrVerde
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingView(navController: NavController) {
    val items = ArrayList<clPageData>()

    items.add(
        clPageData(
            R.raw.login_lottie,
            sTitle = "Accede al sistema",
            sDesc = "Ingresa tu usuario y contraseña, si todavía no cuentas con una, solicitala a tu supervisor",
            bIsLottie = true,
            bIsButton = false
        )
    )

    items.add(
        clPageData(
            R.raw.scan_lottie,
            sTitle = "Escanea tu vehículo",
            sDesc = "Lee el código QR de tu unidad e ingresa el kilometraje con el que iniciaras tu ruta",
            bIsLottie = true,
            bIsButton = false
        )
    )

    items.add(
        clPageData(
            R.raw.itinerario_lottie,
            sTitle = "Genera tu itinerario",
            sDesc = "Genera el itinerario del día, este es el listado de sucursales que debes visitar durante la jornada",
            bIsLottie = true,
            bIsButton = false
        )
    )

    items.add(
        clPageData(
            R.raw.truck_lottie,
            sTitle = "Realiza la visita",
            sDesc = "Sigue los pasos del registro de la recolección marcando tu llegada, tomando fotos de evidencia, firma del cliente y tipo de recolección",
            bIsLottie = true,
            bIsButton = false
        )
    )

    items.add(
        clPageData(
            R.raw.check_lottie,
            sTitle = "Proceso terminado",
            sDesc = "Una vez terminada la recolección de todas las sucursales has teminado con el itinerario del día. Da click en iniciar para comenzar con el trabajo",
            bIsLottie = true,
            bIsButton = true
        )
    )

    val pagerState = rememberPagerState(
        pageCount = { items.size },
        initialPage = 0
    )


    val context = LocalContext.current
    val dataStore = clDataStore(context)
    Box(modifier = Modifier)
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(state = pagerState) { page ->
                //Contenido de las páginas
                Column(
                    modifier = Modifier
                        .padding(40.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoaderData(
                        modifier = Modifier
                            .size(300.dp)
                            .fillMaxWidth()
                            .align(alignment = Alignment.CenterHorizontally),
                        image = items[page].nImage,
                        bIsLottie = items[page].bIsLottie
                    )
                    Text(
                        text = items[page].sTitle,
                        modifier = Modifier
                            .padding(vertical = 30.dp),
                        color = clrGrisOscuro,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = items[page].sDesc,
                        modifier = Modifier
                            .height(150.dp),
                        color = clrGrisOscuro,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light,
                        fontSize = 20.sp

                    )
                }
            }
            PageIndicator(items.size, pagerState.currentPage)
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter))
        {
            //BOTON
            Row(
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = if (!items[pagerState.currentPage].bIsButton) Arrangement.SpaceBetween else Arrangement.Center
            ) {
                if (items[pagerState.currentPage].bIsButton) {
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                dataStore.saveStoreBoard(true)
                            }
                            //Ir a Login y no regresar a acá
                            navController.navigate("Login") {
                                popUpTo(0)
                            }


                        },
                        shape = RoundedCornerShape(20),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = clrVerde
                        )
                    ) {
                        Text(
                            text = "Iniciar",
                            modifier = Modifier
                                .padding(vertical = 10.dp, horizontal = 40.dp),
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun Indicator(bIsSelect: Boolean) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .height(15.dp)
            .width(15.dp)
            .clip(CircleShape)
            .background(if (bIsSelect) clrAzul else Color.Gray)

    )
}

@Composable
fun PageIndicator(nSize: Int, nCurrentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(top = 30.dp, bottom = 30.dp)
    ) {
        repeat(nSize)
        {
            Indicator(bIsSelect = it == nCurrentPage)
        }
    }
}