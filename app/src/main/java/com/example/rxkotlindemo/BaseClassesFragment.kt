package com.example.rxkotlindemo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.rxkotlindemo.databinding.FragmentBaseClassesBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class BaseClassesFragment :  Fragment() {

    private lateinit var binding: FragmentBaseClassesBinding

    private lateinit var navController : NavController

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBaseClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewBinding()
    }

    private fun viewBinding() {
        binding.btnSingle.setOnClickListener {
            onHandleSingle()
        }
        binding.btnCompletable.setOnClickListener {
            onHandleCompletable()
        }

        binding.btnMaybe.setOnClickListener {
            onHandleMaybe()
        }

        binding.btnObservable.setOnClickListener {
            onHandleObservable()
        }

        binding.btnFlowable.setOnClickListener {
            onHandleFlowable()
        }
    }

    //Single
    private fun onHandleSingle() {
        binding.tvOperationType.text = getString(R.string.single)
        val inputString = "Single get result success or error"
        binding.tvInput.text = inputString
        val disposable = dataSourceForSingle()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.tvOutput.text = it.toString()
            }, {
                binding.tvOutput.text = it.toString()
            })

        compositeDisposable.add(disposable)
    }

    private fun dataSourceForSingle(): Single<List<Int>> {
        return Single.create { subscriber ->
            val list = listOf(1, 2, 3, 4, 5)
            subscriber.onSuccess(list)
        }
    }

    //Completable
    private fun onHandleCompletable() {
        binding.tvOperationType.text = getString(R.string.completable)
        val inputString = "Completable get result completable or error"
        binding.tvInput.text = inputString
        val disposable = dataSourceForCompletable()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.tvOutput.text = "result completable"
            }, {
                binding.tvOutput.text = "result error"
            })

        compositeDisposable.add(disposable)
    }

    private fun dataSourceForCompletable(): Completable {
        return Completable.create { subscriber ->
            subscriber.onComplete()
        }
    }

    //Maybe
    private fun onHandleMaybe() {
        binding.tvOperationType.text = getString(R.string.maybe)
        val inputString = "Maybe get result success or completable or error"
        binding.tvInput.text = inputString
        val disposable = dataSourceForMaybe()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.tvOutput.text = it.toString()
            }, {
                binding.tvOutput.text = it.toString()
            }, {
                binding.tvOutput.text = "result completable"
            })

        compositeDisposable.add(disposable)
    }

    private fun dataSourceForMaybe(): Maybe<List<Int>> {
        return Maybe.create { subscriber ->
            val list = listOf(1, 2, 3, 4)
            subscriber.onSuccess(list)
            subscriber.onComplete()
        }
    }

    //Observable
    private fun onHandleObservable() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.observable)
        val inputString = "Observed events every 0.1 second from 1 to 10"
        binding.tvInput.text = inputString
        val disposable = dataSourceForObservable()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.tvOutput.append("$it ") // onNext
                if (it< 10){
                    binding.hsv.visibility = View.INVISIBLE
                } else{
                    binding.hsv.visibility = View.VISIBLE
                }
            }, {
                binding.tvOutput.text = it.toString()
            })


        compositeDisposable.add(disposable)
    }

    private fun dataSourceForObservable(): Observable<Int> {
        return Observable.create { subscriber ->
            for (i in 1..10) {
                subscriber.onNext(i)
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    //Flowable
    private fun onHandleFlowable() {
        binding.tvOperationType.text = getString(R.string.flowable)
        val inputString = "Use Flowable with BackpressureStrategy.LATEST to observed events from 1 to 900000"
        binding.tvInput.text = inputString
        val disposable = dataSourceFlowable()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.tvOutput.text = it.toString() // onNext
                Log.e("Log", "ivent =" + it)
            }, {
                binding.tvOutput.text = it.toString()
            })
        compositeDisposable.add(disposable)
    }

    private fun dataSourceFlowable(): Flowable<Int> {
        return Flowable.create({ subscriber ->
            for (i in 1..900000) {
                subscriber.onNext(i)
            }
           subscriber.onComplete()
        }, BackpressureStrategy.LATEST)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}