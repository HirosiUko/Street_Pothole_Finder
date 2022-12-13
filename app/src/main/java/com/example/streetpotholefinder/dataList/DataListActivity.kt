package com.example.streetpotholefinder.dataList

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dinuscxj.progressbar.CircleProgressBar
import com.example.streetpotholefinder.R
import com.example.streetpotholefinder.R.id.data_list_view
import com.example.streetpotholefinder.RecResultActivity
//import com.example.streetpotholefinder.databinding.AccidentlistBinding.inflate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.datetime.LocalDateTime
import java.io.Serializable
import java.lang.Float.min

class DataListActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var adapter: DataListAdapter
    var contentList = mutableListOf<DataListVO>()
    private lateinit var progressStreet : CircleProgressBar
    private lateinit var clayoutProgress : androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_list)

        val rv = findViewById<RecyclerView>(data_list_view)


        // clayoutProgress
        clayoutProgress = findViewById(R.id.clayoutProgress)

        // Progress bar
        progressStreet = findViewById(R.id.progressStreet)
        // Progress bar initialization
        progressStreet.max=100
        progressStreet.progress = 0    //현재 프로그레스 값
        progressStreet.setProgressFormatter { progress, max ->
            val DEFAULT_PATTERN = "%d"
            String.format(DEFAULT_PATTERN, (progress.toFloat() / max.toFloat() * 100).toInt())
        }
        progressStreet.visibility = View.VISIBLE

        auth = FirebaseAuth.getInstance()
        var fireStore = Firebase.firestore.collection(auth.currentUser?.displayName ?: "devmode")
        fireStore.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    progressStreet.progress = 50
//                    Log.d(TAG, "FB " + document.documents)

                    for ( tmp_doc in document.documents) {
                        Log.d(TAG, "received data from FB " + tmp_doc.data)

                        var _date: String = ""
                        var _time: String = ""
                        var _durate: String = ""
                        var _cntPothole: Long = 0
                        var _cntCrack: Long = 0

                        tmp_doc.data!!.forEach { (key, value) ->
                            when (key) {
                                "recStartTime" -> {
                                    _date = LocalDateTime.parse(value as String).date.toString()
                                    _time = LocalDateTime.parse(value).hour.toString() + "시" +
                                            LocalDateTime.parse(value).minute.toString() + "분" +
                                            LocalDateTime.parse(value).second.toString() + "초"
                                }
                                "cntCrack" -> {
                                    _cntCrack = value as Long
                                }
                                "cntPothole" -> {
                                    _cntPothole = value as Long
                                }
                            }
                        }
                        contentList.add(
                            DataListVO(
                                _date, _time, "0", _cntPothole.toString(), _cntCrack.toString(),
                                tmp_doc.id,
                            )
                        )
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Log.d(TAG, "No such document")
                    Toast.makeText(this, "There is no stored data.", Toast.LENGTH_LONG).show()
                }
                clayoutProgress.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                Toast.makeText(
                    this,
                    "Firebase is not able to access : $exception",
                    Toast.LENGTH_LONG
                ).show()
                clayoutProgress.visibility = View.GONE
            }

        adapter = DataListAdapter(contentList)

        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv.setHasFixedSize(true)

        adapter = DataListAdapter(contentList)

        rv.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        var swipeHelperCallback = SwipeHelperCallback().apply { setClamp(250f) }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(rv)

        rv.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(rv)
            false
        }

        rv.adapter = adapter

    }

}

class DataListAdapter(val dataList: MutableList<DataListVO>) :
    RecyclerView.Adapter<DataListAdapter.CustomViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.data_list_one_lyt, parent, false)
        return CustomViewHolder(view).apply {
            llytSwipeView.setOnClickListener {

                var curpos: Int = adapterPosition
                var intent = Intent(context, RecResultActivity::class.java)

                intent.putExtra("previousActivityInfo", "DataListAdapter")
                intent.putExtra("ref", dataList[curpos])

                context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.tvRecordLength.text = dataList.get(position).strRecordLength
        holder.tvStreetDate.text = dataList.get(position).strStreetDate
        holder.tvStreetTime.text = dataList.get(position).strStreetTime
        holder.tvPotholeCnt.text = dataList.get(position).strPotholeCnt
        holder.tvCrackCnt.text = dataList.get(position).strCrackCnt

        holder.onDeleteClick = {
            Toast.makeText(context, "삭제되었습니다", Toast.LENGTH_SHORT).show()
            removeItem(it)
        }
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        var position = viewHolder.adapterPosition


        var auth = FirebaseAuth.getInstance()
        Log.d("DataListAdapter", "removeItem: "+dataList.get(position).eventRef)

        var doc = Firebase.firestore.collection(auth.currentUser?.displayName ?: "devmode")
            .document(dataList.get(position).eventRef).collection("pothole")

        doc.get().addOnSuccessListener {
            it.documents.forEach{
                doc.document(it.id).delete().addOnCanceledListener {
                    Log.d("DataListAdapter", "DocumentSnapshot successfully deleted!")
                }
            }
            doc.document().delete()
        }
//
//        Firebase.firestore.collection(auth.currentUser?.displayName ?: "devmode")
//            .document(dataList.get(position).eventRef).collection("crack").document().delete()
//            .addOnSuccessListener { Log.d("DataListAdapter", "DocumentSnapshot successfully deleted!") }
//            .addOnFailureListener { e -> Log.w("DataListAdapter", "Error deleting document", e) }

        Firebase.firestore.collection(auth.currentUser?.displayName ?: "devmode")
            .document(dataList.get(position).eventRef).delete()
            .addOnSuccessListener { Log.d("DataListAdapter", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("DataListAdapter", "Error deleting document", e) }

        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStreetDate: TextView = itemView.findViewById<TextView>(R.id.StreetDate)
        val tvStreetTime: TextView = itemView.findViewById<TextView>(R.id.StreetTime)
        val tvRecordLength: TextView = itemView.findViewById<TextView>(R.id.RecordLength)
        val tvPotholeCnt: TextView = itemView.findViewById<TextView>(R.id.PotholeCnt)
        val tvCrackCnt: TextView = itemView.findViewById<TextView>(R.id.CrackCnt)
        val btnDel: ImageView = itemView.findViewById<ImageView>(R.id.btndel)
        val llytSwipeView: LinearLayout = itemView.findViewById<LinearLayout>(R.id.llyt_swipe_view)

        var onDeleteClick: ((RecyclerView.ViewHolder) -> Unit)? = null

        init {
            btnDel.setOnClickListener {
                onDeleteClick?.let { onDeleteClick ->
                    onDeleteClick(this)
                }
            }
        }

    }
}

class DataListVO(
    val strStreetDate: String,
    val strStreetTime: String,
    val strRecordLength: String,
    val strPotholeCnt: String,
    val strCrackCnt: String,
    val eventRef: String,
) : Serializable

class SwipeHelperCallback : ItemTouchHelper.Callback() {

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f

    override fun getMovementFlags(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentDx = 0f
        getDefaultUIUtil().clearView(getView(viewHolder))
        previousPosition = viewHolder.adapterPosition
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            currentPosition = viewHolder.adapterPosition
            getDefaultUIUtil().onSelected(getView(it))
        }
    }

    // view밖으로 escape되는 것 막기
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 10
    }

    // view밖으로 escape되는 것 막기
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val isClamped = getTag(viewHolder)
        // 현재 View가 고정되어있지 않고 사용자가 -clamp 이상 swipe시 isClamped true로 변경 아닐시 false로 변경
        setTag(viewHolder, !isClamped && currentDx <= -clamp)
        return 2f
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val isClamped = getTag(viewHolder)  //고정할지 말지 결정, true : 고정함  false:고정안함
            val x = clampViewPositionHorizontal(
                view, dX, isClamped, isCurrentlyActive
            )    //x만큼 이동(고정 해제 시 이동위치 결정)

            // 고정시킬 시 애니메이션 추가
            if (x == -clamp) {
                getView(viewHolder).animate().translationX(-clamp).setDuration(100L).start()
                return
            }

            currentDx = x
            getDefaultUIUtil().onDraw(
                c, recyclerView, view, dX, dY, actionState, isCurrentlyActive
            )
        }
    }


    private fun clampViewPositionHorizontal(
        view: View, dX: Float, isClamped: Boolean, isCurrentlyActive: Boolean
    ): Float {

        // RIGHT 방향으로 swipe 막기
        val max: Float = 0f

        //고정할 수 있으면
        val x = if (isClamped) {
            // View가 고정되었을 때 swipe되는 영역 제한
            if (isCurrentlyActive) dX - clamp else -clamp
        } else {
            // 고정할 수 없으면 x는 스와이프한 만큼
            dX / 2
        }
        //return min(max(min,x),max)
        return min(x, max)
    }

    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        // isClamped를 view의 tag로 관리
        viewHolder.itemView.tag = isClamped
    }

    private fun getTag(viewHolder: RecyclerView.ViewHolder): Boolean {
        // isClamped를 view의 tag로 관리
        return viewHolder.itemView.tag as? Boolean ?: false
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View {
        return (viewHolder as DataListAdapter.CustomViewHolder).itemView.findViewById(R.id.llyt_swipe_view)
    }

    fun setClamp(clamp: Float) {
        this.clamp = clamp
    }

    // 다른 View가 swipe 되거나 터치되면 고정 해제
    fun removePreviousClamp(recyclerView: RecyclerView) {
        if (currentPosition == previousPosition) return
        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).translationX = 0f
            setTag(viewHolder, false)
            previousPosition = null
        }
    }



}






