package com.example.weatherapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.api.ApiClient;
import com.example.api.WeatherResponse;
import com.example.weatherapp.databinding.FragmentWeatherFragmentBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentWeatherFragmentBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment weather_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Инициализация DataBinding
        binding = FragmentWeatherFragmentBinding.inflate(inflater, container, false);

        // Получаем данные о погоде через Retrofit
        String city = "London"; // Город для отображения погоды
        String apiKey = "ce7bdd804c17555df83f0e852398bbcd"; // Ваш API ключ
        String units = "metric"; // Единицы измерения (например, Celsius)

        // Выполняем запрос к API через Retrofit
        ApiClient.getApiService().getWeather(city, apiKey, units).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                // Проверка успешности ответа
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherData = response.body();
                    // Привязка данных о погоде к UI
                    binding.setWeatherData(weatherData); // Это будет автоматически обновлять UI
                } else {
                    // Ошибка при получении данных
                    Toast.makeText(getActivity(), "Error retrieving data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Ошибка при подключении
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot(); // Возвращаем корневой элемент DataBinding
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Очищаем binding
    }
}