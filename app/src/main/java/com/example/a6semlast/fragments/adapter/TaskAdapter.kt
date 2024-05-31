import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.a6semlast.R
import com.example.a6semlast.Task

class TaskAdapter(context: Context, private val tasks: MutableList<Task>) :
    ArrayAdapter<Task>(context, 0, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val task = tasks[position]

        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
        val textViewDescription = view.findViewById<TextView>(R.id.textViewDescription)
        val checkBoxCompleted = view.findViewById<CheckBox>(R.id.checkBoxCompleted)

        textViewTitle.text = task.title
        textViewDescription.text = task.description
        checkBoxCompleted.isChecked = task.completed

        return view
    }

    fun filter(text: String) {
        val filteredTasks = if (text.isEmpty()) {
            tasks.toList() // Вернуть полный список, если текст пустой
        } else {
            tasks.filter { task ->
                task.title.contains(text, ignoreCase = true) || task.description.contains(text, ignoreCase = true)
            }
        }
        clear() // Очистить текущий список
        addAll(filteredTasks) // Добавить отфильтрованные задачи в адаптер
        notifyDataSetChanged() // Обновить отображение списка
    }
}
