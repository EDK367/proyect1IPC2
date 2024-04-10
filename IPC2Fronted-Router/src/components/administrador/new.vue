<template>
  <div class="pa-4 text-center">
    <v-dialog v-model="dialog" max-width="600">
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
          class="text-none font-weight-regular"
          prepend-icon="mdi-account"
          text="New User"
          variant="tonal"
          v-bind="activatorProps"
        ></v-btn>
      </template>

      <v-card prepend-icon="mdi-account" title="New User">
        <v-card-text>
          <v-row dense>
            <v-col cols="12" md="4" sm="6">
              <v-text-field
              v-model="idAdmin"
               :rules="[rules.required]"
                label="CUI*"
                type="number"

                required
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-text-field 
            v-model="nombre"
              :rules="[rules.required]"
              label="First name*"
            
               required
               ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-text-field
             v-model="apellido"
              :rules="[rules.required]" 
                label="Last name*"

                required
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-text-field 
              v-model="correo"
              :rules="[rules.required]"
              label="Email*" 
          
              required
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="4" sm="6">
              <v-text-field
              v-model="contraseña"
              :rules="[rules.required]"
                label="Password*"
                type="password"
                required
              ></v-text-field>
            </v-col>


            <v-col cols="12" sm="6"> </v-col>
          </v-row>

          <small class="text-caption text-medium-emphasis"
            >*indicates required field</small
          >
        </v-card-text>

        <v-col
              cols="12"
              sm="6"
            >
              <v-select
                v-model="rank"
                :rules="[rules.required]"
                :items="['Admin', 'Operator']"
                label="Rank*"
                required
              ></v-select>
            </v-col>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn 
          text="Close" 
          variant="plain" 
          @click= "closeDialogAndClearFields"
          ></v-btn>



          <v-btn
            color="primary"
            text="Save"
            variant="tonal"
            :disabled = "!idAdmin || !nombre || !apellido || !correo || !contraseña || !rank"
            @click="saveDialogAndSubmitForm"
          ></v-btn>


        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  data: () => ({
    dialog: false,
    idAdmin: "",
    nombre: "",
    apellido: "",
    correo: "",
    contraseña: "",
    rank: "",
    rules: {
      required: (value) => !!value || "Field is required",
    },
  }),

methods: {
closeDialogAndClearFields(){
this.dialog = false,
this.clearTextFields();
},

  clearTextFields(){
    this.idAdmin = '';
    this.nombre = '';
    this.apellido = '';
    this.correo = '';
    this.contraseña = '';
    this.rank = '';
  },
//aca esta la logica para guardar los datos y responder segun sea lo necesario
saveDialogAndSubmitForm(){
  this.submitForm();
  
},

  submitForm(){
    
    const create = {
        cuiAdmin: this.idAdmin,
        nombre: this.nombre,
        apellido: this.apellido,
        correo: this.correo,
        contraseña: this.contraseña
    };
    const jsonUser = JSON.stringify(create);
    console.log(create)

//detecta para donde ira el post
    if(this.rank === 'Admin'){
      console.log("Admin")
      axios
       .post('http://localhost:8080/backendIPC2/api/admin', create)
  .then((response) => {
    console.log(response.data)
    if (Object.keys(response.data).length > 0) {
      console.log("Usuario creado exitosamente");
     
      this.dialog = false;
      // Aquí puedes mostrar un mensaje al usuario o realizar alguna acción adicional
    } else {
      console.log("No se pudo crear");
      // Aquí puedes mostrar un mensaje de éxito al usuario o realizar alguna acción adicional
    }
  })
  .catch((error) => {
    console.error("Error al enviar la solicitud:", error);
    // Aquí puedes manejar errores de conexión u otros errores y mostrar un mensaje al usuario si es necesario
  });


    }else if(this.rank === 'Operator'){
      console.log("Operator")
    }else{
      console.log("No funciono")
    }
  },



}


};
</script>

<style></style>
