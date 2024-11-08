package fun.destywen.houry.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.net.InetAddress;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import fun.destywen.houry.R;

public class RemoteFileFragment extends Fragment {

    private static final String TAG = "c_o_l_u_h";
    private TextView statusText;
    WifiManager.MulticastLock multicastLock;
    private JmDNS jmDNS;

    public RemoteFileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WifiManager wifiManager = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        multicastLock = wifiManager.createMulticastLock("myMulticastLock");
        multicastLock.setReferenceCounted(true);
        multicastLock.acquire();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_remote_file, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        statusText = view.findViewById(R.id.remote_status);
        new Thread(() -> {
            try {
                jmDNS = JmDNS.create(InetAddress.getLocalHost());
                statusText.setText("JmDNS created");
                jmDNS.addServiceListener("_http._tcp.local.", new ServiceListener() {
                    @Override
                    public void serviceAdded(ServiceEvent event) {
                        statusText.setText("add " + event.getName());
                        Log.d(TAG, "serviceAdded: " + event.getInfo());
                    }

                    @Override
                    public void serviceRemoved(ServiceEvent event) {
                        Log.d(TAG, "serviceRemoved: " + event.getInfo());
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void serviceResolved(ServiceEvent event) {
                        ServiceInfo serviceInfo = event.getInfo();
                        String ipAddress = serviceInfo.getInet4Addresses()[0].getHostAddress();
                        int port = serviceInfo.getPort();
                        Log.d(TAG, "serviceResolved: " + ipAddress + ":" + port);
                        statusText.setText(ipAddress + ":" + port);
                        // ...
                    }
                });
                Thread.sleep(60000);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (multicastLock != null) {
            multicastLock.release();
        }
    }
}
