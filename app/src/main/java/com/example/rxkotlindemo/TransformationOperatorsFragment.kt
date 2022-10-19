package com.example.rxkotlindemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rxkotlindemo.databinding.FragmentTransformationOperatorsBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.functions.Function

class TransformationOperatorsFragment : Fragment() {

    private lateinit var binding: FragmentTransformationOperatorsBinding

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTransformationOperatorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding()
    }

    private fun viewBinding() {
        binding.btnMap.setOnClickListener {
            onHandleMap()
        }

        binding.btnFlatMap.setOnClickListener {
            onHandleFlatMap()
        }

        binding.btnConcatMap.setOnClickListener {
            onHandleConcatMap()
        }

        binding.btnSwitchMap.setOnClickListener {
            onHandleSwitchMap()
        }

        binding.btnBuffer.setOnClickListener {
            onHandleBuffer()
        }

        binding.btnGroupBy.setOnClickListener {
            onHandleGroupBy()
        }


        binding.btnScan.setOnClickListener {
            onHandleScan()
        }
    }

    private fun onHandleMap(){
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.map)
        val inputString = "Observed events \"Ukraine\", \"USA\", \"Poland\" - add the country to each"
        binding.tvInput.text = inputString
        val list = listOf("Ukraine", "USA", "Poland")
        val disposable = Observable.fromIterable(list)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map { "\"country $it\" " }
            .subscribe({
                binding.tvOutput.append(it)
            }, {
                binding.tvOutput.text = it.toString()
            })
        compositeDisposable.add(disposable)
    }

    private fun modifyObservable(input: Int) =
        Observable.create(ObservableOnSubscribe<Int> { emitter ->
            emitter.onNext(input * input)
            emitter.onComplete()
        }).subscribeOn(Schedulers.io())

    private fun onHandleFlatMap(){
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.flatmap)
        val builder = StringBuilder()
        val inputString = "Observed events every from 1 to 5 - value * value"
        binding.tvInput.text = inputString
        val list = listOf(1, 2, 3, 4, 5)
        val disposable = Observable.fromIterable(list)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { modifyObservable(it) }
            .subscribe({ value ->
                builder.append("$value ")
            }, { error ->
                binding.tvOutput.text = error.message
            }, {
                binding.tvOutput.text = builder
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleConcatMap(){
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.concat_map)
        val builder = StringBuilder()
        val inputString = "Observed events every from 1 to 5 - value * value"
        binding.tvInput.text = inputString
        val list = listOf(1, 2, 3, 4, 5)
        val disposable = Observable.fromIterable(list)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .concatMap { modifyObservable(it) }
            .subscribe({ value ->
                builder.append("$value ")
            }, { error ->
                binding.tvOutput.text = error.message
            }, {
                binding.tvOutput.text = builder
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleSwitchMap(){
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.switch_map)
        val builder = StringBuilder()
        val inputString = "Observed events every from 1 to 5 - value * value"
        binding.tvInput.text = inputString
        val list = listOf(1, 2, 3, 4, 5)
        val disposable = Observable.fromIterable(list)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap { modifyObservable(it) }
            .subscribe({ value ->
                builder.append("$value ")
            }, { error ->
                binding.tvOutput.text = error.message
            }, {
                binding.tvOutput.text = builder
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleBuffer(){
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.buffer)
        val inputString = "Observed events listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10) - buffer 3"
        binding.tvInput.text = inputString
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val disposable = Observable.fromIterable(list)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .buffer(3)
            .subscribe({
                binding.tvOutput.append("$it ")
            }, {
                binding.tvOutput.text = it.toString()
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleGroupBy(){
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.group_by)
        val inputString = "Observed events Ukraine, USA, Poland - groupBy \"U\""
        binding.tvInput.text = inputString
        val disposable: Disposable = Observable.just("Ukraine", "USA", "Poland")
            .groupBy(object : Function<String, Boolean> {
                override fun apply(p0: String): Boolean {
                    return p0.contains("U")
                }
            })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                if (result.key == true) {
                    result.subscribe {
                        binding.tvOutput.append("${it} ")
                    }
                }
            }, { error ->
                binding.tvOutput.text = error.message
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleScan(){
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.scan)
        val inputString = "Observed events fom 1 to 5 - use scan calculate factorial"
        binding.tvInput.text = inputString
        val disposable: Disposable = Observable.just(1, 2, 3, 4, 5)
            .scan(object : BiFunction<Int, Int, Int> {
                override fun apply(t1: Int, t2: Int): Int {
                    return t1 * t2
                }

            })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("$result ")
            }, { error ->
                binding.tvOutput.text = error.message
            })
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}