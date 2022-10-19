package com.example.rxkotlindemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rxkotlindemo.databinding.FragmentConcatenateOperatorsBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CombinationOperatorsFragment : Fragment() {

    private lateinit var binding: FragmentConcatenateOperatorsBinding

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentConcatenateOperatorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding()
    }

    private fun viewBinding() {
        binding.btnZipWith.setOnClickListener {
            onHandleZipWith()
        }

        binding.btnMergeWith.setOnClickListener {
            onHandleMergeWith()
        }

        binding.btnCombineLatest.setOnClickListener {
            onHandleCombineLatest()
        }

        binding.btnConcatWith.setOnClickListener {
            onHandleConcatWith()
        }
    }

    private fun onHandleZipWith() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.zip_with)
        val inputString = " val goods = Observable.just(\"bread\", \"apple\", \"banana\", \"owen\")\n" +
                "        val price = Observable.just(\"2usd\", \"1usd\", \"3usd\", \"50usd\")\n" +
                "        val disposable = goods.zipWith(price, object : BiFunction<String, String, String> {\n" +
                "            override fun apply(t1: String, t2: String): String {\n" +
                "                return if (t1 == \"owen\") {\n" +
                "                    \"Owen is not available\"\n" +
                "                } else {\n" +
                "                    \"$"+"t1"+ "-" + "$" +"t2"+"\n" +
                "                }\n" +
                "            }\n" +
                "        })"
        binding.tvInput.text = inputString
        val goods = Observable.just("bread", "apple", "banana", "owen")
        val price = Observable.just("2usd", "1usd", "3usd", "50usd")
        val disposable = goods.zipWith(price, object : BiFunction<String, String, String> {
            override fun apply(t1: String, t2: String): String {
                return if (t1 == "owen") {
                    "Owen is not available"
                } else {
                    "$t1 - $t2"
                }
            }
        }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("$result ") // onNext
            }, { error ->
                binding.tvOutput.text = error.message
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleMergeWith() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.merge_with)
        val inputString = "val arabic = Observable.just(\"1\", \"2\", \"3\", \"4\", \"5\")\n" +
                "val rome = Observable.just(\"I\", \"II\", \"III\", \"IV\", \"V\")\n" +
                "compositeDisposable.add(\n" +
                "eventsArabic.zipWith(Observable.interval(300, TimeUnit.MILLISECONDS),\n" +
                "BiFunction<String, Any, String> { t1, t2 -> t1 }).mergeWith(\n" +
                "eventsRome.zipWith(Observable.interval(500, TimeUnit.MILLISECONDS),\n" +
                "BiFunction<String, Any, String> { t1, t2 -> t1 })\n" +
                ")"
        binding.tvInput.text = inputString
        val arabic = Observable.just("1", "2", "3", "4", "5")
        val rome = Observable.just("I", "II", "III", "IV", "V")
        compositeDisposable.add(
            arabic.zipWith(Observable.interval(300, TimeUnit.MILLISECONDS),
                BiFunction<String, Any, String> { t1, t2 -> t1 }).mergeWith(
                rome.zipWith(Observable.interval(500, TimeUnit.MILLISECONDS),
                    BiFunction<String, Any, String> { t1, t2 -> t1 })
            ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        binding.tvOutput.append("$result ") // onNext
                    }, { error ->
                        binding.tvOutput.text = error.message
                    }
                )
        )
    }

    private fun onHandleCombineLatest() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.combine_latest)
        val inputString = "val temperatureFirstFactory = Observable.just(10, 15, 20, 25, 30)\n" +
                "        val temperatureSecondFactory = Observable.just(-10, -15, -20, -25, -30)\n" +
                "        compositeDisposable.add(\n" +
                "            Observable.combineLatest(\n" +
                "                temperatureFirstFactory.zipWith(\n" +
                "                    Observable.interval(300, TimeUnit.MILLISECONDS)\n" +
                "                ) { t1, t2 -> t1 },\n" +
                "                temperatureSecondFactory.zipWith(\n" +
                "                    Observable.interval(500, TimeUnit.MILLISECONDS)\n" +
                "                ) { t1, t2 -> t1 }\n" +
                "            ) { t1, t2 -> arrayOf(t1, t2) }"
        binding.tvInput.text = inputString
        val temperatureFirstFactory = Observable.just(10, 15, 20, 25, 30)
        val temperatureSecondFactory = Observable.just(-10, -15, -20, -25, -30)
        compositeDisposable.add(
            Observable.combineLatest(
                temperatureFirstFactory.zipWith(Observable.interval(300, TimeUnit.MILLISECONDS),
                    object : BiFunction<Int, Any, Int> {
                        override fun apply(t1: Int, t2: Any): Int {
                            return t1
                        }
                    }
                ),
                temperatureSecondFactory.zipWith(Observable.interval(500, TimeUnit.MILLISECONDS),
                    object : BiFunction<Int, Any, Int> {
                        override fun apply(t1: Int, t2: Any): Int {
                            return t1
                        }
                    }
                ),
                object : BiFunction<Int, Int, Array<Int>> {
                    override fun apply(t1: Int, t2: Int): Array<Int> {
                        return arrayOf(t1, t2)
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        binding.tvOutput.text =
                            "temperatureFirstFactory =" + result[0] + " temperatureSecondFactory = " + result[1]
                    }, { error ->
                        binding.tvOutput.text = error.message
                    }
                )
        )
    }

    private fun onHandleConcatWith(){
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.concat_with)
        val inputString = "val arabic = Observable.just(\"1\", \"2\", \"3\", \"4\", \"5\")\n" +
                "val rome = Observable.just(\"I\", \"II\", \"III\", \"IV\", \"V\")\n" +
                "compositeDisposable.add(\n" +
                "eventsArabic.zipWith(Observable.interval(300, TimeUnit.MILLISECONDS),\n" +
                "BiFunction<String, Any, String> { t1, t2 -> t1 }).concatWith(\n" +
                "eventsRome.zipWith(Observable.interval(500, TimeUnit.MILLISECONDS),\n" +
                "BiFunction<String, Any, String> { t1, t2 -> t1 })\n" +
                ")"
        binding.tvInput.text = inputString
        val arabic = Observable.just("1", "2", "3", "4", "5")
        val rome = Observable.just("I", "II", "III", "IV", "V")
        compositeDisposable.add(
            arabic.zipWith(Observable.interval(300, TimeUnit.MILLISECONDS),
                BiFunction<String, Any, String> { t1, t2 -> t1 }).concatWith(
                rome.zipWith(Observable.interval(500, TimeUnit.MILLISECONDS),
                    BiFunction<String, Any, String> { t1, t2 -> t1 })
            ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        binding.tvOutput.append("$result ") // onNext
                    }, { error ->
                        binding.tvOutput.text = error.message
                    }
                )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}