package com.example.rxkotlindemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rxkotlindemo.databinding.FragmentHelperOperatorsBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Predicate
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class HelperOperatorsFragment : Fragment() {

    private lateinit var binding: FragmentHelperOperatorsBinding

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHelperOperatorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding()
    }

    private fun viewBinding() {
        binding.btnAll.setOnClickListener {
            onHandleAll()
        }

        binding.btnContains.setOnClickListener {
            onHandleContains()
        }

        binding.btnDelay.setOnClickListener{
            onHandleDelay()
        }

        binding.btnDefaultIfEmpty.setOnClickListener {
            onHandleDefaultIfEmpty()
        }

        binding.btnTimeInterval.setOnClickListener {
            onHandleTimeInterval()
        }

        binding.btnTimeStamp.setOnClickListener {
            onHandleTimeStamp()
        }

        binding.btnTimeOut.setOnClickListener {
            onHandleTimeOut()
        }
    }

    private fun onHandleAll() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.all)
        val inputString = "Observed events \"Ukraine\", \"USA\", \"Poland\" - .all(Predicate { it.contains(\"Ukraine\") })"
        binding.tvInput.text = inputString
        val disposable = Observable.just("Ukraine", "USA", "Poland")
            .all(Predicate { it.contains("Ukraine") })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("$result ") // onNext
            }, { error ->
                binding.tvOutput.append(error.message)
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleContains() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.contains)
        val inputString = "Observed events 1, 2, 3 - .contains(\"3\")"
        binding.tvInput.text = inputString
        val disposable = Observable.just(1,2,3)
            .contains(3)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("$result ") // onNext
            }, { error ->
                binding.tvOutput.append(error.message)
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleDelay() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.delay)
        val inputString = "Observed events 1, 2, 3 - .delay(2000, TimeUnit.MILLISECONDS)"
        binding.tvInput.text = inputString
        val disposable = Observable.just(1,2,3)
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("$result ") // onNext
            }, { error ->
                binding.tvOutput.append(error.message)
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleDefaultIfEmpty() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.default_if_empty)
        val inputString = "Observed events 1 - .skip(1)\n.defaultIfEmpty(\"Events not found\")"
        binding.tvInput.text = inputString
        val disposable = Observable.just("")
            .skip(1)
            .defaultIfEmpty("Events not found")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("$result ") // onNext
            }, { error ->
                binding.tvOutput.append(error.message)
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleTimeInterval() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.time_interval)
        val inputString = "val disposable = Observable.just(\"User1\",\"User2\",\"User3\")\n" +
                "            .zipWith(Observable.interval(300, TimeUnit.MILLISECONDS)) { t1, t2 -> t1 }\n" +
                "            .timeInterval()"
        binding.tvInput.text = inputString
        val disposable = Observable.just("User1","User2","User3")
            .zipWith(Observable.interval(300, TimeUnit.MILLISECONDS)) { t1, t2 -> t1 }
            .timeInterval()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("result value -> ${result.value()}  result.time -> ${result.time()} ") // onNext
            }, { error ->
                binding.tvOutput.append(error.message)
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleTimeStamp() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.time_stamp)
        val inputString = "val disposable = Observable.just(\"User1\",\"User2\",\"User3\")\n" +
                "            .zipWith(Observable.interval(300, TimeUnit.MILLISECONDS)) { t1, t2 -> t1 }\n" +
                "            .timestamp()"
        binding.tvInput.text = inputString
        val disposable = Observable.just("User1","User2","User3")
            .zipWith(Observable.interval(300, TimeUnit.MILLISECONDS)) { t1, t2 -> t1 }
            .timestamp()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("result value -> ${result.value()}  result.time -> ${result.time()} ") // onNext
            }, { error ->
                binding.tvOutput.append(error.message)
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleTimeOut() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.time_out)
        val inputString = "val disposable = Observable.just(\"User1\",\"User2\",\"User3\")\n" +
                "            .zipWith(Observable.interval(300, TimeUnit.MILLISECONDS)) { t1, t2 -> t1 }\n" +
                "            .timeout(250, TimeUnit.MILLISECONDS)"
        binding.tvInput.text = inputString
        val disposable = Observable.just("User1", "User2", "User3")
            .zipWith(Observable.interval(300, TimeUnit.MILLISECONDS)) { t1, t2 -> t1 }
            .timeout(250, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("event -> ${result}   ") // onNext
            }, { error ->
                binding.tvOutput.append(error.message)
            })
        compositeDisposable.add(disposable)
    }
}