package pat.international.playwaretwo;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;


public class HomeFragment extends Fragment implements OnAntEventListener {

        private MotoConnection connection;

        private boolean isPairing = false;
        private TextView tilesNumText;
        private int numOfTiles;
        private NavController nav;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        connection = MotoConnection.getInstance();
        connection.startMotoConnection(getContext());
        connection.saveRfFrequency(68); // See the back of your tile for your group’s RF
        connection.setDeviceId(7); //Your group number
        //connection.registerListener(this);
        Button startGame = view.findViewById(R.id.button_game);
        tilesNumText = view.findViewById(R.id.textNumTilesStart);
        Button pairingButton = view.findViewById(R.id.button_start);
        pairingButton.setOnClickListener(p->paring(pairingButton));
        nav = Navigation.findNavController(view);
        startGame.setOnClickListener(v-> startGameChase());
    }

    // I have problems reestablishing the connection, how often do I need to pair.
    private void startGameChase(){
        if(numOfTiles>0){
            nav.navigate(R.id.action_mainFragment_to_startChase);
        }
        else{
            Toast.makeText(getContext(), "Please, check if the tiles are connected.", Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    public void onStart() {
        super.onStart();
        //connection.startMotoConnection(getContext());
        connection.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //connection.stopMotoConnection();
        connection.unregisterListener(this);
    }

    private void paring(Button b){
        if(!isPairing){
            connection.pairTilesStart();
            b.setText("Stop Pairing");
        } else {
            connection.pairTilesStop();
            b.setText("Start Pairing");
        }
        isPairing = !isPairing;
    }
    @Override
    public void onMessageReceived(byte[] bytes, long l) {

    }

    @Override
    public void onAntServiceConnected() {
        connection.setAllTilesToInit();
    }

    @Override
    public void onNumbersOfTilesConnected(int i) {
        numOfTiles = i;
        tilesNumText.setText("Number of Tiles connected: "+numOfTiles);
    }
}