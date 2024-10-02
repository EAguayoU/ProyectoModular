package com.imbitbox.recolectora.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.imbitbox.recolectora.R
import com.imbitbox.recolectora.ui.theme.*

/*SPACERS*/
@Composable
fun HSpace(nSpace: Int = 10) {
    Spacer(modifier = Modifier.width(nSpace.dp))
}

@Composable
fun VSpace(nSpace: Int = 10) {
    Spacer(modifier = Modifier.height(nSpace.dp))
}

/* TEXT FIELDS */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdtField(
    modifier: Modifier = Modifier,
    label: String = "",
    bReadOnly: Boolean = false,
    color: Color = Color.Black,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    placeholder: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        readOnly = bReadOnly,
        //singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = color,
            unfocusedTextColor = color,
            unfocusedPlaceholderColor = color
        ),
        placeholder = { Text(text = placeholder)},
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        keyboardActions = KeyboardActions(
            onDone = { println("Realizo On Done ") }
        ),
        modifier = modifier
    )
}

@Composable
fun EdtHintField(
    modifier: Modifier = Modifier,
    label: String,
    bReadOnly: Boolean = false,
    color: Color = Color.Black,
    bShowValue: Boolean,
    sText: String,
    onValueChange: (String) -> Unit,
    onShowChange: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = sText,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        readOnly = bReadOnly,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = color,
            unfocusedTextColor = color
        ),
        visualTransformation = if (bShowValue) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            if (bShowValue) {
                IconButton(onClick = onShowChange) {
                    Icon(
                        painter = painterResource(id = R.drawable.eye_hidden),
                        contentDescription = "mostrar valor"
                    )
                }
            } else {
                IconButton(onClick = onShowChange) {
                    Icon(
                        painter = painterResource(id = R.drawable.eye_view),
                        contentDescription = "mostrar valor"
                    )
                }
            }
        }
    )
}


/*BOTONES */
@Composable
fun BtnStandar(
    modifier: Modifier = Modifier,
    modifierText: Modifier = Modifier,
    sText: String = "",
    nFontSize: Int = 15,
    nRoundedCorner: Int = 0,
    containerColor: Color = clrAzul,
    contentColor: Color = Color.White,
    bIsClickable: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = if (bIsClickable) {
            onClick
        } else {
            { }
        },
        shape = RoundedCornerShape(nRoundedCorner),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
    ) {
        Text(
            text = sText,
            fontSize = nFontSize.sp,
            modifier = modifierText
        )
    }
}

@Composable
fun BtnStandar(
    modifier: Modifier = Modifier,
    modifierText: Modifier = Modifier,
    modifierIcon: Modifier = Modifier,
    sText: String = "",
    nFontSize: Int = 15,
    nRoundedCorner: Int = 0,
    containerColor: Color = clrAzul,
    contentColor: Color = Color.White,
    painterIcon: Int,
    bIsClickable: Boolean = true,
    bIsEnable : Boolean = true,
    onClick: () -> Unit,

    ) {
    Button(
        onClick = if (bIsClickable) {
            onClick
        } else {
            { }
        },
        shape = RoundedCornerShape(nRoundedCorner),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        enabled = bIsEnable,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = painterIcon),
            contentDescription = sText,
            modifier = modifierIcon
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = sText,
            fontSize = nFontSize.sp,
            modifier = modifierText
        )
    }
}

@Composable
fun BtnStandar(
    modifier: Modifier = Modifier,
    modifierText: Modifier = Modifier,
    modifierIcon: Modifier = Modifier,
    sText: String = "",
    nFontSize: Int = 15,
    nRoundedCorner: Int = 0,
    containerColor: Color = clrAzul,
    contentColor: Color = Color.White,
    imageVector: ImageVector,
    bIsClickable: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = if (bIsClickable) {
            onClick
        } else {
            { }
        },
        shape = RoundedCornerShape(nRoundedCorner),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = sText,
            modifier = modifierIcon
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = sText,
            fontSize = nFontSize.sp,
            modifier = modifierText
        )
    }
}

@Composable
fun BtnIcon(
    modifier: Modifier = Modifier,
    modifierIcon: Modifier = Modifier,
    contentDescription: String = "",
    tint: Color = Color.White,
    painterIcon: Int,
    bIsClickable: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        onClick = if (bIsClickable) {
            onClick
        }
        else{{ }},
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(painterIcon),
            contentDescription = contentDescription,
            tint = tint,
            modifier = modifierIcon
        )
    }
}

@Composable
fun BtnIcon(
    modifier: Modifier = Modifier,
    modifierIcon: Modifier = Modifier,
    contentDescription: String = "",
    tint: Color = Color.White,
    imageVector: ImageVector,
    bIsClickable: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        onClick = if (bIsClickable) {
            onClick
        } else {
            { }
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
            modifier = modifierIcon
        )
    }
}


/*SWITCH */
@Composable
fun BtnSwitch(
    bEnabled: Boolean = true,
    cColor: Color = clrAzul,
    bChecked: Boolean,
    onCheckedValue: (Boolean) -> Unit
) {
    Switch(
        enabled = bEnabled,
        checked = bChecked,
        onCheckedChange = onCheckedValue,
        thumbContent = if (bChecked) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = cColor,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        } else {
            null
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = cColor,
            uncheckedThumbColor = Color.White,
            uncheckedTrackColor = Color.Gray
        )
    )
}

//Loading
@Composable
fun BoxLoading(bHasBackground : Boolean = true, sText: String = "") {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(Color(0x66888888))
            .fillMaxSize()
            .padding(20.dp)
    ) {
        val nBoxWidth = this.maxWidth-20.dp
        val nBoxHeight = this.maxHeight-20.dp
        val modifier : Modifier

        if(bHasBackground) {
            modifier = Modifier
                .width(nBoxWidth - 150.dp)
                .height(nBoxHeight - 600.dp)
                .clip(shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp))
                .background(Color(0xF1FFFFFF))
        }
        else
        {
            modifier = Modifier
                .width(nBoxWidth - 150.dp)
                .height(nBoxHeight - 600.dp)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoaderData(
                    modifier = Modifier.fillMaxWidth(),
                    image = R.raw.blocksloading_lottie,
                    bIsLottie = true
                )
                if(sText != "") {
                    Text(
                        sText,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
//Loader Lotties
@Composable
fun LoaderData(modifier: Modifier, image: Int, bIsLottie: Boolean) {
    if (bIsLottie) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(image)
        )

        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = modifier
        )
    } else {
        Image(painter = painterResource(id = image), contentDescription = "", modifier = modifier)
    }
}

//No hay Datos
@Composable
fun NoDataView(){
    Box(modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    )
    {
        LoaderData(modifier = Modifier.fillMaxWidth(), image = R.raw.nodata_lottie, bIsLottie = true )
        //VSpace(100)
        Text(
            modifier = Modifier
                .height(270.dp)
                .align(Alignment.BottomCenter),
            text = "No hay Información",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp)
    }
}

//Alertas
@Composable
fun AlertDialogYesNo(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(

        icon = {
            Icon(icon, contentDescription = "")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AlertDialogOnly(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Ok")
            }
        }
    )
}