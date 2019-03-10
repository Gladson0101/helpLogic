package com.example.helplogic.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.helplogic.R;
import com.example.helplogic.config.FirebaseConfig;
import com.example.helplogic.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextSenha;
    private EditText editTextConfirmarSenha;

    private Button buttonConcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editTextNome = findViewById(R.id.editTextNome);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        editTextConfirmarSenha = findViewById(R.id.editTextConfimarSenha);

        buttonConcluir = findViewById(R.id.botaoConcluir);
        buttonConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarUsuario();
            }
        });

    }

    public void cadastrarUsuario() {

        // Recupera os campos.
        String nome = editTextNome.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();
        String confirmarSenha = editTextConfirmarSenha.getText().toString();

        // Variável de controle.
        boolean valido = true;

        // Verifica se os campos estão vazios.
        if (nome.isEmpty()) {
            editTextNome.setError("Campo obrigatório!");
            valido = false;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Campo obrigatório!");
            valido = false;
        }

        if (senha.isEmpty()) {
            editTextSenha.setError("Campo obrigatório!");
            valido = false;
        }

        if (confirmarSenha.isEmpty()) {
            editTextConfirmarSenha.setError("Campo obrigatório!");
            valido = false;
        }

        // Verifica se a verificação de senha coincide com a senha.
        if (!senha.equals(confirmarSenha)) {
            editTextSenha.setError("As senhas não coincidem!");
            editTextConfirmarSenha.setError("As senhas não coincidem!");
            valido = false;
        }

        // Se todos os dados estiverem corretos, realiza o cadastro.
        if (valido) {

            // Cria uma nova instância de usuário.
            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(senha);

            // Obtém a instência de autenticação do usuário e cria um novo usuário se o mesmo não existir.
            auth = FirebaseConfig.getFirebaseAuth();
            auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            Toast.makeText(CadastroActivity.this, "Senha Fraca!", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(CadastroActivity.this, "Email Inválido!", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthUserCollisionException e) {
                            Toast.makeText(CadastroActivity.this, "Usuário já cadastrado!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(CadastroActivity.this, "Não foi possível cadastrar o usuário!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }

    }

}