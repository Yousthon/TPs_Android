package imt_atlantique.example.com.tp_4_3;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

    public class EditFragment extends Fragment {


        // declaration de callback pour pouvoir creer un lien direct avec notre activité
        private OnGreenFragmentListener mCallback;


        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_edit, container, false);

            Button button = v.findViewById(R.id.btnAfficher);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText nom = getView().findViewById(R.id.textViewNom);
                    String message = nom.getText().toString();
                    mCallback.onDisplayFragment(message);
                    //Toast.makeText(getView().getContext(), nom.getText().toString(),Toast.LENGTH_LONG).show();
                }
            });
            return v;

        }

        // creation de l'interface pour obliger les activités souhaintantes
        //communiquer avec notre fragment a implementer la methode onDisplayFragment
        //notre fragment peut donc communiquer avec notre interface.
        public interface OnGreenFragmentListener {
            void onDisplayFragment(String text);
        }

        // This method insures that the Activity has actually implemented our
        // listener and that it isn't null.
//permet d'attacher notre fragment a son activité parente
        //en creant un callback pointant vers notre activité
        //notre activité parente implemente l'interface OnGreenFragmentListener
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof OnGreenFragmentListener) {
                mCallback = (OnGreenFragmentListener) getActivity();
            } else {
                throw new RuntimeException(context.toString()
                        + " doit implementer OnGreenFragmentListener");
            }
        }


        public void onDetach() {
            super.onDetach();
            mCallback = null;
        }
    }
