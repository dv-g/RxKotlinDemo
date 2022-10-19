package com.example.rxkotlindemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rxkotlindemo.databinding.FragmentFilterOperatorsBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class FilterOperatorsFragment : Fragment() {

    private lateinit var binding: FragmentFilterOperatorsBinding

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFilterOperatorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding()
    }

    private fun viewBinding() {
        binding.btnFilter.setOnClickListener{
            onHandleFilter()
        }
        binding.btnDistinct.setOnClickListener {
            onHandleDistinct()
        }

        binding.btnElementAt.setOnClickListener {
            onHandleElementAt()
        }

        binding.btnSkip.setOnClickListener {
            onHandleSkip()
        }

        binding.btnSkipLast.setOnClickListener {
            onHandleSkipLast()
        }

        binding.btnTake.setOnClickListener {
            onHandleTake()
        }

        binding.btnTake.setOnClickListener {
            onHandleTake()
        }

        binding.btnTakeLast.setOnClickListener {
            onHandleTakeLast()
        }

        binding.btnTakeWhile.setOnClickListener {
            onHandleTakeWhile()
        }

        binding.btnDebounce.setOnClickListener {
            onHandleDebounce()
        }
    }

    //Filter the odd ones
    private fun onHandleFilter() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.filter)
        val inputString = "Observed events from 1 to 10 filter the odd ones"
        binding.tvInput.text = inputString
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val disposable = Observable.fromIterable(list)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .filter{ it -> it.rem(2) != 0}
            .subscribe({
                binding.tvOutput.append("$it ")
            }, {
                binding.tvOutput.text = it.toString()
            })

        compositeDisposable.add(disposable)
    }

    // ElementAt - 3 event from given list
    private fun onHandleElementAt() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.element_at)
        val inputString = "Observed events 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 Event at 3 from given list"
        binding.tvInput.text = inputString
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val disposable = Observable.fromIterable(list)
            .elementAt(3)
            .subscribe({
                binding.tvOutput.append("$it ")
            }, {
                binding.tvOutput.text = it.toString()
            })
        compositeDisposable.add(disposable)
    }

    // Skip - Skip 3 events from given list
    private fun onHandleSkip() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.skip)
        val inputString = "Observed events 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 Skip 3 events from given list"
        binding.tvInput.text = inputString
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val disposable = Observable.fromIterable(list)
            .skip(3)
            .subscribe({
                binding.tvOutput.append("$it ")
            }, {
                binding.tvOutput.text = it.toString()
            })
        compositeDisposable.add(disposable)
    }

    // Skip Last  - Skip last 3 events from given list
    private fun onHandleSkipLast() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.skip_last)
        val inputString = "Observed events 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 Skip last 3 events from given list"
        binding.tvInput.text = inputString
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val disposable = Observable.fromIterable(list)
            .skipLast(3)
            .subscribe({
                binding.tvOutput.append("$it ")
            }, {
                binding.tvOutput.text = it.toString()
            })
        compositeDisposable.add(disposable)
    }

    //Distinct
    private fun onHandleDistinct() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.distinct)
        val inputString = "Observed events 1, 1, 2, 3, 4, 5, 5, 5, 6"
        binding.tvInput.text = inputString
        val list = listOf(1, 1, 2, 3, 4, 5, 5, 5, 6)
        val disposable = Observable.fromIterable(list)
            .distinct()
            .subscribe({
                binding.tvOutput.append("$it ")
            }, {
                binding.tvOutput.text = it.toString()
            })
        compositeDisposable.add(disposable)
    }

    //Take - 4 events from given list
    private fun onHandleTake() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.take)
        val list = listOf(1, 2, 2, 5, 5, 6, 7, 7, 9, 9)
        val inputString = "Observed events 1, 2, 2, 5, 5, 6, 7, 7, 9, 9 take 4 events"
        binding.tvInput.text = inputString
        val disposable = Observable.fromIterable(list)
            .take(4)
            .subscribe({
                binding.tvOutput.append("$it ")
            }, {
                binding.tvOutput.text = it.message.toString()
            })
        compositeDisposable.add(disposable)
    }

    //Take Last - 4 last events from given list
    private fun onHandleTakeLast() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.take_last)
        val list = listOf(1, 2, 2, 5, 5, 6, 7, 7, 9, 9)
        val inputString = "Observed events 1, 2, 2, 5, 5, 6, 7, 7, 9, 9 take last 4 events"
        binding.tvInput.text = inputString
        val disposable = Observable.fromIterable(list)
            .takeLast(4)
            .subscribe({
                binding.tvOutput.append("$it ")
            }, {
                binding.tvOutput.text = it.message.toString()
            })
        compositeDisposable.add(disposable)
    }

    //TakeWhile - until odd number occurs
    private fun onHandleTakeWhile() {
        binding.tvOutput.text = ""
        val list = listOf(2, 2, 2, 4, 4, 5, 6, 7, 8, 8)
        binding.tvOperationType.text = getString(R.string.take_while)
        val inputString = "Observed events 2, 2, 2, 4, 4, 5, 6, 7, 8, 8 - until odd number occurs { t -> t.rem(2) == 0 }"
        binding.tvInput.text = inputString
        val disposable = Observable.fromIterable(list)
            .takeWhile { t -> t.rem(2) == 0 }
            .subscribe({
                binding.tvOutput.append("$it ")
            }, {
                binding.tvOutput.text = it.message.toString()
            })
        compositeDisposable.add(disposable)
    }

    //Debounce
    private fun onHandleDebounce() {
        binding.tvOutput.text = ""
        val list = listOf(2, 2, 2, 4, 4, 5, 6, 7, 8, 8)
        binding.tvOperationType.text = getString(R.string.debounce)
        val inputString =
            "Observed events every 100 millisecond from 1 to 7 - debounce(150, TimeUnit.MILLISECONDS)"
        binding.tvInput.text = inputString
        val disposable = dataSourceForObservable()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(150, TimeUnit.MILLISECONDS)
            .subscribe({
                binding.tvOutput.append("$it ")
                if (it< 10){
                    binding.hsv.visibility = View.INVISIBLE
                } else{
                    binding.hsv.visibility = View.VISIBLE
                }
            },{
                binding.tvOutput.text = it.message.toString()
            })
        compositeDisposable.add(disposable)
    }

    private fun dataSourceForObservable(): Observable<Int> {
        return Observable.create { subscriber ->
            for (i in 1..7) {
                subscriber.onNext(i)
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
